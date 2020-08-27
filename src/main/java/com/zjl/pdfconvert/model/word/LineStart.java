package com.zjl.pdfconvert.model.word;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.style.Align;

/**
 * @author Zhu jialiang
 * @date 2020/8/21
 */
public class LineStart implements Fact {
    private int lineIndex;
    private Align align;
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

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }
}
