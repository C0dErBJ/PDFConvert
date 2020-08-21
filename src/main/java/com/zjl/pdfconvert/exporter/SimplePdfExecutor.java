package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.parser.Parser;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class SimplePdfExecutor implements PdfExecutor {

    private Parser pdfParser;

    public SimplePdfExecutor(Parser parser) {
        this.pdfParser = parser;
    }

    @Override
    public void doExport() {

    }

    @Override
    public void doParse() {

    }

}
