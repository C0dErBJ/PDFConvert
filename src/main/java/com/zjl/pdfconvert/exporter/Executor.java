package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.parser.Parser;

import java.io.OutputStream;

/**
 * @author Zhu jialiang
 * @date 2020/8/31
 */
public interface Executor {

    String add(Parser parser, Exporter exporter);

    ExportFileModel getExportFile(String uuid);
}
