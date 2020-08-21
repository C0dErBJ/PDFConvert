package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.Fact;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class PdfParser implements Parser {

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
            CustomTextStripper stripper = null;
            try {
                stripper = new CustomTextStripper();
                PDTrueTypeFont.load(document, new File("C:\\Windows\\Fonts\\simsunb.ttf"), Encoding.getInstance(COSName.WIN_ANSI_ENCODING));
            } catch (IOException e) {
                e.printStackTrace();
                return facts;
            }

            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            try {
                stripper.getText(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
            facts = stripper.getWords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return facts;

    }


}
