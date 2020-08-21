package com.zjl.pdfconvert.model.table;

import com.zjl.pdfconvert.model.Element;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Cell extends Element {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
