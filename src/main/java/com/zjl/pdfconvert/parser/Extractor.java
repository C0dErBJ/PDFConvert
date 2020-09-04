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

    /**
     * 如果没有特殊处理，则返回本extractor提取的元素
     *
     * @param currentFacts 当前所有已经提取的pdf元素
     * @return 经过pipeline方法处理之后的所有元素
     */
    List<T> pipeline(List<Fact> currentFacts);
}
