package com.zjl.pdfconvert.parser.directory;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.Extractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/9/4
 */
public class OutlineExtractor implements Extractor<Fact> {
    @Override
    public Integer getOrder() {
        return 4;
    }

    @Override
    public void doExtract(PDPage page, int pageIndex) throws IOException {

    }

    @Override
    public void clearCache() {

    }

    @Override
    public List<Fact> pipeline(List<Fact> currentFacts) {
        return currentFacts;
    }

    public static void main(String[] args) throws IOException {
        try(PDDocument document=PDDocument.load(new File("C:\\01.Projects\\01.Code\\pdfconvert\\src\\test\\resources\\test3.pdf"))){
            PDDocumentCatalog catalog=document.getDocumentCatalog();
            //获取PDDocumentOutline文档纲要对象
            PDDocumentOutline outline=catalog.getDocumentOutline();
            PDOutlineItem item=outline.getFirstChild();
        }
    }
}
