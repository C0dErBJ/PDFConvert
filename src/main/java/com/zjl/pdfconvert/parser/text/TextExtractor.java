package com.zjl.pdfconvert.parser.text;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.Style;
import com.zjl.pdfconvert.model.style.Align;
import com.zjl.pdfconvert.model.word.LineBreak;
import com.zjl.pdfconvert.model.word.LineStart;
import com.zjl.pdfconvert.model.word.Word;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import sun.awt.FontDescriptor;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/14
 */
public class TextExtractor extends CustomPDFTextStripper {
    private List<Fact> words = new LinkedList<Fact>();
    private int cursor = 0;

    public TextExtractor() throws IOException {
        addOperator(new SetStrokingColor());
        addOperator(new SetStrokingColorSpace());
        addOperator(new SetNonStrokingColorSpace());
        addOperator(new SetStrokingDeviceCMYKColor());
        addOperator(new SetNonStrokingDeviceCMYKColor());
        addOperator(new SetNonStrokingDeviceRGBColor());
        addOperator(new SetStrokingDeviceRGBColor());
        addOperator(new SetNonStrokingDeviceGrayColor());
        addOperator(new SetStrokingDeviceGrayColor());
        addOperator(new SetStrokingColor());
        addOperator(new SetStrokingColorN());
        addOperator(new SetNonStrokingColor());
        addOperator(new SetNonStrokingColorN());
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        PDColor strokingColor = getGraphicsState().getStrokingColor();
        Word word = new Word();
        word.setText(text.getUnicode());
        Style style = new Style();
        PDFontDescriptor fontDescriptor = text.getFont().getFontDescriptor();
        style.setBold(fontDescriptor.isForceBold());
        style.setItalics(fontDescriptor.isItalic());
        style.setFontFamily(text.getFont().getFontDescriptor().getFontFamily());
        style.setFontSize(text.getFontSize());
        style.setX(text.getXDirAdj());
        style.setY(text.getYDirAdj());
        float[] rgb = strokingColor.getComponents();
        if (rgb.length == 3) {
            style.setColor(String.format("%02x", (int) (rgb[0] * 255))
                    + String.format("%02x", (int) (rgb[1] * 255))
                    + String.format("%02x", (int) (rgb[2] * 255)));
        }
        word.setStyle(style);
        this.words.add(word);
        super.processTextPosition(text);
    }

    @Override
    public void writeLine(List<CustomPDFTextStripper.WordWithTextPositions> line) throws IOException {
        int numberOfStrings = line.size();
        int textCount = 0;
        LineStart ls = new LineStart();
        if (line.get(0).getTextPositions().get(0).getX() != 0) {
            ls.setAlign(Align.CENTER);
        }
        this.words.add(new LineStart());
        for (int i = 0; i < numberOfStrings; i++) {
            WordWithTextPositions word = line.get(i);
            textCount += word.getTextPositions().size();
            super.writeString(word.getText(), word.getTextPositions());
            if (i < numberOfStrings - 1) {
                super.writeWordSeparator();
            }
        }
        if (!line.isEmpty()) {
            this.words.add(cursor + textCount, new LineBreak());
            cursor += textCount + 1;
        }
        super.writeLine(line);
    }

    public List<Fact> getWords() {
        return this.words;
    }

    public void clearCache() {
        this.words = new LinkedList<>();
        this.cursor = 0;
    }
}
