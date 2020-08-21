package com.zjl.pdfconvert.model.word;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.style.Align;

/**
 * @author Zhu jialiang
 * @date 2020/8/21
 */
public class LineStart implements Fact {
    private Align align;

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }
}
