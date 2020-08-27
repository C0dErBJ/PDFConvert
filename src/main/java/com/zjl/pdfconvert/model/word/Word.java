package com.zjl.pdfconvert.model.word;

import com.zjl.pdfconvert.model.Element;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Word extends Element {
    private String text;
    private int lineIndex;

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
