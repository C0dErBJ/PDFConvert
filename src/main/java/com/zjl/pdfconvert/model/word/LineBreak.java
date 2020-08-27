package com.zjl.pdfconvert.model.word;

import com.zjl.pdfconvert.model.Fact;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class LineBreak implements Fact {
    private int lineIndex;
    private final static String LINEBREAK = System.getProperty("line.separator");
    private int pageNo;
    private Fact word;

    public Fact getWord() {
        return word;
    }

    public void setWord(Fact word) {
        this.word = word;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }
}
