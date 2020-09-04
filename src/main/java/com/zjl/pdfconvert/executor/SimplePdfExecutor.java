package com.zjl.pdfconvert.executor;

import com.zjl.pdfconvert.exporter.Exporter;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.Parser;

import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class SimplePdfExecutor implements Executor<byte[]> {

    public SimplePdfExecutor() {
    }

    @Override
    public byte[] doExecutor(Parser parser, Exporter exporter) {
        List<Fact> facts = parser.doParse();
        for (Fact fact : facts) {
            exporter.doExport(fact);
        }
        return exporter.writeByte();
    }

}
