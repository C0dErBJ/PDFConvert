package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.executor.ParseCompleteListener;
import com.zjl.pdfconvert.model.ArticleEnd;
import com.zjl.pdfconvert.model.ArticleStart;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.image.ImageExtractor;
import com.zjl.pdfconvert.parser.table.TableExtractor;
import com.zjl.pdfconvert.parser.text.TextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.BlockingDeque;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class ContentParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(ContentParser.class);
    private BlockingDeque<Fact> factBlockingDeque;
    private InputStream is;
    private List<Extractor> extractors;
    private String fileName;
    private List<Fact> parsedFacts;
    private ParseCompleteListener parseCompleteListener;


    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ContentParser() {
        this.init();
    }

    @Override
    public List<Fact> getParsedFacts() {
        return parsedFacts;
    }

    private void init() {
        this.extractors = new LinkedList<>();
        this.parsedFacts = new ArrayList<>();
        TextExtractor textExtractor = new TextExtractor();
        textExtractor.setSortByPosition(true);
        this.extractors.add(textExtractor);
        this.extractors.add(new ImageExtractor());
        this.extractors.add(new TableExtractor());
    }

    public ContentParser(InputStream is) {
        this.is = is;
        this.init();

    }


    @Override
    public void setFactBlockingDeque(BlockingDeque<Fact> factBlockingDeque) {
        this.factBlockingDeque = factBlockingDeque;
    }

    @Override
    public void setInputStream(InputStream is) {
        this.is = is;
    }

    @Override
    public void addExtractor(Extractor extractor) {
        this.extractors.add(extractor);
        this.extractors.sort(Comparator.comparing(Extractor::getOrder));
    }

    @Override
    public void removeExtractorByOrder(int order) {
        for (Iterator<Extractor> dd = this.extractors.iterator(); dd.hasNext(); ) {
            Integer o = dd.next().getOrder();
            if (o.equals(order)) {
                dd.remove();
            }
        }
    }

    @Override
    public List<Fact> doParse() {

        this.parsedFacts.add(new ArticleStart());
        try (PDDocument document = PDDocument.load(this.is)) {
            int pages = document.getNumberOfPages();
            for (int i = 0; i < pages; i++) {
                for (Extractor<Fact> extractor : this.extractors) {
                    extractor.doExtract(document.getPage(i), i);
                    List<Fact> factList = extractor.pipeline(this.parsedFacts);
                    this.parsedFacts.addAll(factList);
                    extractor.clearCache();
                }
            }
            this.parseCompleteListener.onComplete(this.parsedFacts);
            logger.info("------------解析完成-------------");
        } catch (IOException e) {
            logger.error("解析发生问题", e);
        } finally {
            if (this.is != null) {
                try {
                    this.is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.parsedFacts.add(new ArticleEnd());
        return this.parsedFacts;

    }


    @Override
    public void run() {
        List<Fact> facts = this.doParse();
        for (int i = 0; i < facts.size(); i++) {
            if (facts.get(i) == null) {
                logger.info("队列内包含null值");
            }
            this.factBlockingDeque.offerLast(facts.get(i));
        }
        logger.info("------------推送完成-------------");
    }

    @Override
    public void onComplete(ParseCompleteListener callBack) {
        this.parseCompleteListener = callBack;
    }

}
