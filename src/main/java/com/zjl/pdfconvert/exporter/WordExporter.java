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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class WordExporter implements Exporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordExporter.class);
    private XWPFDocument document = new XWPFDocument();
    private XWPFParagraph currentParagraph;
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
    public void doExport(Fact fact) {
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
        XWPFRun xwpfRun = currentParagraph.createRun();
        if (fact instanceof Word) {
            xwpfRun.setText(((Word) fact).getText());
            xwpfRun.setBold(((Word) fact).getStyle().isBold());
            xwpfRun.setItalic(((Word) fact).getStyle().isItalics());
            xwpfRun.setColor(((Word) fact).getStyle().getColor());
            CTShd ctShd = xwpfRun.getCTR().addNewRPr().addNewShd();
            ctShd.setVal(STShd.CLEAR);
            ctShd.setColor("auto");
            ctShd.setFill(((Word) fact).getStyle().getBackgroundColor());
            xwpfRun.setFontFamily(((Word) fact).getStyle().getFontFamily().getName());
            xwpfRun.setFontSize((int) ((Word) fact).getStyle().getFontSize());
        }
        if (fact instanceof LineBreak) {
          // 换行处理
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
            LOGGER.info("图片");
            try {
                try (ByteArrayInputStream basis = new ByteArrayInputStream(((Image) fact).getFile())) {
                    xwpfRun.addPicture(basis,
                            XWPFDocument.PICTURE_TYPE_PNG,
                            ((Image) fact).getFileName(),
                            Units.toEMU(((Image) fact).getDisplayWidth()),
                            Units.toEMU(((Image) fact).getDisplayHeight()));

                }
            } catch (InvalidFormatException | IOException e) {
                LOGGER.error("转换IO异常", e);
            }
        }

    }

    @Override
    public void writeFile(String filePath) {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            this.document.write(outputStream);
        } catch (IOException e) {
            LOGGER.error("IO输出异常", e);
        }
    }

    @Override
    public byte[] writeByte() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            this.document.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    @Override
    public void run() {
        while (true) {
            Fact fact = this.factBlockingDeque.pollFirst();
            this.doExport(fact);
            if (fact instanceof ArticleEnd) {
                break;
            }
        }
    }
}
