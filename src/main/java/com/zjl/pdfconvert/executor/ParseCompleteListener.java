package com.zjl.pdfconvert.executor;

import com.zjl.pdfconvert.model.Fact;

import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/9/4
 */
public interface ParseCompleteListener {
    void onComplete(List<Fact> factList);
}
