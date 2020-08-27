package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.Fact;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/26
 */
public interface Extractor {

    List<Fact> doExtract(PDDocument document);
}
