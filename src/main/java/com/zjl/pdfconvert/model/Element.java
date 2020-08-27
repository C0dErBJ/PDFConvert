package com.zjl.pdfconvert.model;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public abstract class Element implements Fact {
    private int pageNo;
    private Style style;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
