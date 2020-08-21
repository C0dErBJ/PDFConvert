package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.Image;
import com.zjl.pdfconvert.model.style.Align;
import com.zjl.pdfconvert.model.word.LineBreak;
import com.zjl.pdfconvert.model.word.LineStart;
import com.zjl.pdfconvert.model.word.Word;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class WordExporter implements Exporter {
    private ThreadLocal<XWPFDocument> document = new ThreadLocal<>();
    private String filePath;
    private XWPFParagraph currentParagraph;
    private XWPFRun currentXWPFRun;

    public WordExporter() {
        this.filePath = filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void export(Fact fact) {
        if (fact == null) {
            return;
        }
        if (this.document.get() == null) {
            XWPFDocument doc = new XWPFDocument();
            this.document.set(doc);
        }
        if (document.get().getParagraphs().isEmpty()) {
            currentParagraph = this.document.get().createParagraph();
        } else {
            currentParagraph = this.document.get().getLastParagraph();
        }
        currentXWPFRun = currentParagraph.createRun();
        if (fact instanceof LineStart) {
            if (((LineStart) fact).getAlign() == Align.CENTER) {
                currentParagraph.setAlignment(ParagraphAlignment.CENTER);
            } else if (((LineStart) fact).getAlign() == Align.LEFT) {
                currentParagraph.setAlignment(ParagraphAlignment.LEFT);
            } else if (((LineStart) fact).getAlign() == Align.RIGHT) {
                currentParagraph.setAlignment(ParagraphAlignment.RIGHT);
            }
        }

        if (fact instanceof Word) {
            System.out.print(((Word) fact).getText());
            currentXWPFRun.setText(((Word) fact).getText());
            currentXWPFRun.setBold(((Word) fact).getStyle().isBold());
            currentXWPFRun.setItalic(((Word) fact).getStyle().isItalics());
            currentXWPFRun.setColor(((Word) fact).getStyle().getColor());
            currentXWPFRun.setFontFamily(((Word) fact).getStyle().getFontFamily());
            currentXWPFRun.setFontSize((int) ((Word) fact).getStyle().getFontSize());
        }
        if (fact instanceof LineBreak) {
            System.out.print("||");
            currentXWPFRun.addCarriageReturn();
        }
        if (fact instanceof Image) {
            System.out.print("图片  ");
            try {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(((Image) fact).getFile())) {
                    XWPFPicture picture = currentXWPFRun.addPicture(bais,
                            XWPFDocument.PICTURE_TYPE_PNG,
                            ((Image) fact).getFileName(),
                            Units.toEMU(((Image) fact).getDisplayWidth()),
                            Units.toEMU(((Image) fact).getDisplayHeight()));

                }
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
