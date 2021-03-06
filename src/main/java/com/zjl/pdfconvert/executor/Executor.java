package com.zjl.pdfconvert.executor;

import com.zjl.pdfconvert.exporter.Exporter;
import com.zjl.pdfconvert.parser.Parser;

/**
 * @author Zhu jialiang
 * @date 2020/9/3
 */
public interface Executor<T, E extends Exporter> {

    T doExecutor(Parser parser, E exporter);
}
