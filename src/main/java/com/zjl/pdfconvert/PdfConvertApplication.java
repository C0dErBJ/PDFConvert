package com.zjl.pdfconvert;


import com.zjl.pdfconvert.exporter.Exporter;
import com.zjl.pdfconvert.exporter.SyncPdfExecutor;
import com.zjl.pdfconvert.exporter.WordExporter;
import com.zjl.pdfconvert.parser.PdfParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;


@SpringBootApplication
public class PdfConvertApplication {

    public static void main(String[] args) {
        //   SpringApplication.run(PdfConvertApplication.class, args);
        PdfParser pdfParser = new PdfParser();
        pdfParser.setFilePath("C:\\Users\\Zhu jialiang\\Desktop\\这.pdf");
        Exporter exporter = new WordExporter();
        exporter.setFilePath("C:\\Users\\Zhu jialiang\\Desktop\\word.docx");
        SyncPdfExecutor syncPdfExecutor = new SyncPdfExecutor(pdfParser, exporter);
        syncPdfExecutor.start();


    }

}
