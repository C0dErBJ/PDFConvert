package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.image.ImageExtractor;
import com.zjl.pdfconvert.parser.table.TableExtractor;
import com.zjl.pdfconvert.parser.text.TextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class PdfParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(PdfParser.class);

    private String filePath;

    public PdfParser() {
        this.filePath = filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Fact> parse() {
        List<Fact> facts = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            int pages = document.getNumberOfPages();
            TextExtractor textExtractor = null;
            try {
                textExtractor = new TextExtractor();
                textExtractor.setSortByPosition(true);
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
        }
        return facts;

    }
}
