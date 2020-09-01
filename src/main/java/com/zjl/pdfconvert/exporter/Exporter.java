package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Fact;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public interface Exporter extends Runnable {

    void export(Fact fact);

    void writeFile(String filePath);

    ByteArrayOutputStream writeByte();

    void setFactBlockingDeque(BlockingDeque<Fact> factBlockingDeque);

    void setUniqueId(String id);

    String getFileName();

}
