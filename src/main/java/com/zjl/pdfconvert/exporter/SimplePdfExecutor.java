package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.parser.Parser;

import java.io.OutputStream;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class SimplePdfExecutor implements Executor {

    private Parser pdfParser;

    public SimplePdfExecutor(Parser parser) {
        this.pdfParser = parser;
    }

    @Override
    public String add(Parser parser, Exporter exporter) {
        return null;
    }

    @Override
    public ExportFileModel getExportFile(String uuid) {
        return null;
    }


}
