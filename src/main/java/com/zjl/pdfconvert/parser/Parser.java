package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.Fact;

import javax.annotation.security.RunAs;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public interface Parser extends Runnable {

    List<Fact> parse();

    String getFileName();

    void setFactBlockingDeque(BlockingDeque<Fact> factBlockingDeque);

    void setInputStream(InputStream is);

}
