package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.Fact;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/9/3
 */
public interface Extractor<T extends Fact> {

    Integer getOrder();

    void doExtract(PDPage page, int pageIndex) throws IOException;

    void clearCache();

    List<T> pipeline(List<Fact> currentFacts);
}
