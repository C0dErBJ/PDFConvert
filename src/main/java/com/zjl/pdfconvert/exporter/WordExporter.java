package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.ArticleEnd;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.Image;
import com.zjl.pdfconvert.model.style.Align;
import com.zjl.pdfconvert.model.table.Table;
import com.zjl.pdfconvert.model.word.LineBreak;
import com.zjl.pdfconvert.model.word.LineStart;
import com.zjl.pdfconvert.model.word.Word;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import java.io.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class WordExporter implements Exporter {
    private XWPFDocument document = new XWPFDocument();
    private XWPFParagraph currentParagraph;
    private XWPFRun currentXWPFRun;
    private BlockingDeque<Fact> factBlockingDeque;
    private String uniqueId = "";
    private String fileName;

    public WordExporter() {
        this.factBlockingDeque = new LinkedBlockingDeque<>();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setFactBlockingDeque(BlockingDeque<Fact> factBlockingDeque) {
        this.factBlockingDeque = factBlockingDeque;
    }

    @Override
    public void setUniqueId(String id) {
        this.uniqueId = id;
    }

    @Override
    public String getFileName() {
        return this.fileName + ".docx";
    }

    @Override
    public void export(Fact fact) {
        if (fact == null) {
            return;
        }
        if (this.document == null) {
            this.document = new XWPFDocument();
        }

        if (fact instanceof LineStart) {
            currentParagraph = this.document.createParagraph();
            if (((LineStart) fact).getAlign() == Align.CENTER) {
                currentParagraph.setAlignment(ParagraphAlignment.CENTER);
            } else if (((LineStart) fact).getAlign() == Align.RIGHT) {
                currentParagraph.setAlignment(ParagraphAlignment.RIGHT);
            } else {
                currentParagraph.setAlignment(ParagraphAlignment.LEFT);
            }
        }
        if (this.document.getParagraphs().isEmpty()) {
            currentParagraph = this.document.createParagraph();
        }
        currentXWPFRun = currentParagraph.createRun();
        if (fact instanceof Word) {
            System.out.print(((Word) fact).getText());
            currentXWPFRun.setText(((Word) fact).getText());
            currentXWPFRun.setBold(((Word) fact).getStyle().isBold());
            currentXWPFRun.setItalic(((Word) fact).getStyle().isItalics());
            currentXWPFRun.setColor(((Word) fact).getStyle().getColor());
            CTShd cTShd = currentXWPFRun.getCTR().addNewRPr().addNewShd();
            cTShd.setVal(STShd.CLEAR);
            cTShd.setColor("auto");
            cTShd.setFill(((Word) fact).getStyle().getBackgroundColor());
            currentXWPFRun.setFontFamily(((Word) fact).getStyle().getFontFamily().getName());
            currentXWPFRun.setFontSize((int) ((Word) fact).getStyle().getFontSize());
        }
        if (fact instanceof LineBreak) {
            System.out.print("||");
            //  currentXWPFRun.addCarriageReturn();
        }
        if (fact instanceof Table) {
            if (!currentParagraph.isEmpty()) {
                currentParagraph = this.document.createParagraph();
            }
            XWPFTable table = this.document.createTable();
            XWPFTableRow row = table.getRow(0);
            for (int i = 0; i < ((Table) fact).getRowCount(); i++) {
                if (i != 0) {
                    row = table.createRow();
                }
                for (int j = 0; j < ((Table) fact).getColumnCount(); j++) {
                    if (j == 0 || i != 0) {
                        row.getCell(j).setText(((Table) fact).getCells()[i][j].getText());
                    }
                    if (i == 0 && j != 0) {
                        row.addNewTableCell().setText(((Table) fact).getCells()[i][j].getText());
                    }
                }
            }
        }
        if (fact instanceof Image) {
            if (!currentParagraph.isEmpty()) {
                currentParagraph = this.document.createParagraph();
            }
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
    public void writeFile(String filePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            this.document.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ByteArrayOutputStream writeByte() {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            this.document.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }


    @Override
    public void run() {
        while (true) {
            Fact fact = this.factBlockingDeque.pollFirst();
            this.export(fact);
            if (fact instanceof ArticleEnd) {
                System.out.println("----------end------------");
                break;
            }
        }
    }
}
