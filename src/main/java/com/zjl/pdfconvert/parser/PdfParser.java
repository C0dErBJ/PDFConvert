package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.ArticleEnd;
import com.zjl.pdfconvert.model.ArticleStart;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.image.ImageExtractor;
import com.zjl.pdfconvert.parser.table.TableExtractor;
import com.zjl.pdfconvert.parser.text.TextExtractor;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class PdfParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(PdfParser.class);
    private BlockingDeque<Fact> factBlockingDeque;
    private InputStream is;
    private String fileName;

    public PdfParser() {
    }

    public PdfParser(InputStream is) {
        this.is = is;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
    public List<Fact> parse() {
        List<Fact> facts = new ArrayList<>();
        facts.add(new ArticleStart());
        try (PDDocument document = PDDocument.load(this.is)) {
            int pages = document.getNumberOfPages();
            TextExtractor textExtractor = null;
            try {
                textExtractor = new TextExtractor();
                textExtractor.setSortByPosition(true);
                if (document.getNumberOfPages() > 0) {
                    PDResources res = document.getPage(0).getResources();
                    for (COSName fontName : res.getFontNames()) {
                        PDFont font = res.getFont(fontName);
                        if (font != null) {
                            String[] fName = font.getName().split("\\+");
                            textExtractor.setDefaultFont(fName[fName.length - 1]);
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return facts;
            }
            ImageExtractor imageExtractor = new ImageExtractor();
            TableExtractor tableExtractor = new TableExtractor();
            for (int i = 0; i < pages; i++) {

                textExtractor.setStartPage(i + 1);
                textExtractor.setEndPage(Math.min(i + 1, pages));
                textExtractor.getText(document);

                List<Fact> words = textExtractor.getWords();
                facts.addAll(words);


                imageExtractor.processPage(document.getPage(i));
                facts.addAll(imageExtractor.getImages());


                tableExtractor.processPage(document.getPage(i));
                if (tableExtractor.hasTable()) {
                    List<Fact> tableFacts = tableExtractor.concatWordCell(facts, i + 1);
                    facts.addAll(tableExtractor.getTables());
                }

                imageExtractor.clearCache();
                textExtractor.clearCache();
                tableExtractor.clearCache();
            }
            System.out.println("------------解析完成-------------");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            if (this.is != null) {
                try {
                    this.is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        facts.add(new ArticleEnd());
        return facts;

    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public void run() {
        List<Fact> facts = this.parse();
        for (int i = 0; i < facts.size(); i++) {
            if (facts.get(i) == null) {
                System.out.println("队列内包含null值");
                System.out.println(i + "前值" + facts.get(i - 1));
            }
            this.factBlockingDeque.offerLast(facts.get(i));
        }
        System.out.println("------------推送完成-------------");
    }
}
