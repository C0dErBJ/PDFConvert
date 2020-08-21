package com.zjl.pdfconvert.model;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public abstract class Element implements Fact {
    private Style style;

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
