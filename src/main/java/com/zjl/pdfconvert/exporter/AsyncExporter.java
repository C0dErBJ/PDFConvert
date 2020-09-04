package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Fact;

import java.util.concurrent.BlockingDeque;

/**
 * @author Zhu jialiang
 * @date 2020/9/4
 */
public interface AsyncExporter extends Runnable, Exporter {
    void setFactBlockingDeque(BlockingDeque<Fact> factBlockingDeque);
}
