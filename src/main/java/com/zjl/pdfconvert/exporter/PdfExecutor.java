package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Element;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.Parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public interface PdfExecutor {

    void doExport();

    void doParse();
}
