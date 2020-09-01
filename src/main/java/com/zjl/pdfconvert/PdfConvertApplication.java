package com.zjl.pdfconvert;


import com.zjl.pdfconvert.exporter.Executor;
import com.zjl.pdfconvert.exporter.SyncPdfExecutor;
import com.zjl.pdfconvert.exporter.WordExporter;
import com.zjl.pdfconvert.parser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Zhu jialiang
 */
@SpringBootApplication
public class PdfConvertApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfConvertApplication.class, args);
    }

}
