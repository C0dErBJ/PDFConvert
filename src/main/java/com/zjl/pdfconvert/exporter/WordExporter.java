package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Element;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.word.LineBreak;
import com.zjl.pdfconvert.model.word.Word;
import com.zjl.pdfconvert.parser.Parser;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class WordExporter implements Exporter {
    private ThreadLocal<XWPFDocument> document = new ThreadLocal<>();
    private String filePath;
    private Parser parser;
//    XWPFDocument doc = new XWPFDocument();
//        try (FileOutputStream out = new FileOutputStream("C:\\01.Projects\\01.Code\\pdfconvert\\target\\simple.docx")) {
//        doc.write(out);
//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }


    public WordExporter() {
        this.filePath = filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void export(Fact fact) {
        if (this.document.get() == null) {
            XWPFDocument doc = new XWPFDocument();
            this.document.set(doc);
        }
        XWPFRun run = null;
        if (document.get().getParagraphs().isEmpty()) {
            run = this.document.get().createParagraph().createRun();
        } else {
            run = this.document.get().getLastParagraph().createRun();
        }

        if (fact instanceof Word) {
            run.setText(((Word) fact).getText());
            run.setBold(((Word) fact).getStyle().isBold());
            run.setItalic(((Word) fact).getStyle().isItalics());
            run.setColor(((Word) fact).getStyle().getColor());
            run.setFontFamily(((Word) fact).getStyle().getFontFamily());
            run.setFontSize((int) ((Word) fact).getStyle().getFontSize());
        }
        if (fact instanceof LineBreak) {
            run.addCarriageReturn();
        }


    }

    @Override
    public void writeFile() {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            this.document.get().write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
