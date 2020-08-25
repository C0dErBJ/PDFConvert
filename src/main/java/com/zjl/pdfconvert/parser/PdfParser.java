package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.image.ImageExtractor;
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
            for (int i = 0; i < pages; i++) {
                textExtractor.setStartPage(i + 1);
                textExtractor.setEndPage(Math.min(i + 1, pages - 1));
                textExtractor.getText(document);

                facts.addAll(textExtractor.getWords());
                textExtractor.clearCache();

                imageExtractor.processPage(document.getPage(i));
                facts.addAll(imageExtractor.getImages());
                imageExtractor.clearCache();
            }
            System.out.println("------------解析完成-------------");

        } catch (IOException e) {
            e.printStackTrace();

        }
        return facts;

    }
}
