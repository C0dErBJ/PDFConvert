package com.zjl.pdfconvert;

import com.zjl.pdfconvert.executor.SimplePdfExecutor;
import com.zjl.pdfconvert.exporter.Exporter;
import com.zjl.pdfconvert.exporter.WordExporter;
import com.zjl.pdfconvert.parser.Parser;
import com.zjl.pdfconvert.parser.PdfParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class PdfconvertApplicationTests {

    @Test
    void pdfParse() throws IOException {
        String importFile = "/test.pdf";
        try (InputStream is = PdfconvertApplicationTests.class.getResourceAsStream(importFile)) {
            Parser parser = new PdfParser();
            parser.setInputStream(is);
            Exporter exporter = new WordExporter();
            SimplePdfExecutor executor = new SimplePdfExecutor();
            byte[] result = executor.doExecutor(parser, exporter);
            Assert.isTrue(result.length > 0, "����ʧ��");
        }

    }

}
