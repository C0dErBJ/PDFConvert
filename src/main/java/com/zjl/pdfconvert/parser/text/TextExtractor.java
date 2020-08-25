package com.zjl.pdfconvert.parser.text;

import com.zjl.pdfconvert.model.Element;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.Style;
import com.zjl.pdfconvert.model.style.Align;
import com.zjl.pdfconvert.model.word.LineBreak;
import com.zjl.pdfconvert.model.word.LineStart;
import com.zjl.pdfconvert.model.word.Word;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.*;

/**
 * @author Zhu jialiang
 * @date 2020/8/14
 */
public class TextExtractor extends CustomPDFTextStripper {
    //误差值，文本居中误差
    private final static float VARIANCE = 15f;
    private List<Fact> words = new LinkedList<Fact>();
    private Deque<Fact> line = new ArrayDeque<>();
    private int cursor = 0;
    private float padLeft = 0;
    private float padRight = 0;


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
        this.line.offerLast(word);
        super.processTextPosition(text);
    }

    @Override
    public void writeLine(List<CustomPDFTextStripper.WordWithTextPositions> line) throws IOException {
        int numberOfStrings = line.size();
        int blankStrCount = 0;
        for (int i = 0; i < numberOfStrings; i++) {
            WordWithTextPositions word = line.get(i);
            cursor += word.getTextPositions().size();
            if (word.getText().trim().isEmpty()) {
                blankStrCount++;
            }
            super.writeString(word.getText(), word.getTextPositions());
            if (i < numberOfStrings - 1) {
                super.writeWordSeparator();
            }
        }
        //空行的换行会被忽略，强制记录
        if (blankStrCount == numberOfStrings) {
            cursor++;
        }
        //禁止套娃😂
        TextPosition lastWordOfLine = line.get(line.size() - 1).getTextPositions()
                .get(line.get(line.size() - 1).getTextPositions().size() - 1);
        super.writeLine(line);
        //如果最后一个字符不是空格，则不进行增加行首行尾的操作
        if (!" ".equals(lastWordOfLine.getUnicode())) {
            return;
        }
        if (padLeft == 0 || padRight == 0) {
            this.getPad();
        }
        LineStart ls = new LineStart();
        this.words.add(ls);

        Fact firstWord = this.line.peekFirst();

        while (cursor > 1) {
            if (!this.line.isEmpty()) {
                this.words.add(this.line.pollFirst());
            }
            cursor--;
        }
        Fact lastWord = this.line.pollFirst();
        cursor--;
        if (lastWord != null) {
            this.words.add(lastWord);
        }
        LineBreak lb = new LineBreak();
        this.words.add(lb);

        if (firstWord != null && within(((Word) (firstWord)).getStyle().getX(), padLeft, VARIANCE)) {
            ls.setAlign(Align.LEFT);
        }
        if (lastWord != null && ls.getAlign() != Align.LEFT && within(((Word) (lastWord)).getStyle().getX(), padRight, VARIANCE)) {
            ls.setAlign(Align.RIGHT);
        }
        if (firstWord != null && lastWord != null) {
            if (within(((Word) (firstWord)).getStyle().getX() - padLeft, padRight - ((Word) (lastWord)).getStyle().getX(),
                    VARIANCE)) {
                ls.setAlign(Align.CENTER);
            }
        }
    }

    private void getPad() {
        TreeMap<Integer, Integer> padCount = new TreeMap<>();
        this.line.stream().forEach(a -> {
            if (a instanceof Element && ((Element) a).getStyle() != null) {
                Integer x = (int) ((Element) a).getStyle().getX();
                Integer c = padCount.get(x);
                if (c != null) {
                    padCount.put(x, c + 1);
                } else {
                    padCount.put(x, 1);
                }
            }
        });
        List<Map.Entry<Integer, Integer>> keyList = new ArrayList<>(padCount.entrySet());
        keyList.sort((o1, o2) -> o1.getValue().equals(o2.getValue())
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()))
        ;
        float pageWidth = this.getCurrentPage().getMediaBox().getWidth();
        for (Map.Entry<Integer, Integer> entry : keyList) {
            if (entry.getKey() <= (pageWidth / 2) && padLeft == 0) {
                padLeft = entry.getKey();
            }
            if (entry.getKey() >= (pageWidth / 2)) {
                padRight = entry.getKey();
            }
            if (padLeft != 0 && padRight != 0) {
                break;
            }
        }
    }

    public List<Fact> getWords() {
        return this.words;
    }

    public void clearCache() {
        this.words = new LinkedList<>();
    }
}
