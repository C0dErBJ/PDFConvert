package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.executor.ParseCompleteListener;
import com.zjl.pdfconvert.model.Fact;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingDeque;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public interface Parser extends Runnable {

    List<Fact> doParse();

    void setFactBlockingDeque(BlockingDeque<Fact> factBlockingDeque);

    void setInputStream(InputStream is);

    void addExtractor(Extractor extractor);

    void removeExtractorByOrder(int order);

    String getFileName();

    List<Fact> getParsedFacts();

    void onComplete(ParseCompleteListener callBack);
}
