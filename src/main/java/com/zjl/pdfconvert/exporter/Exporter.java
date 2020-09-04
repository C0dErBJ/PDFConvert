package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Fact;

import java.util.concurrent.BlockingDeque;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public interface Exporter  {

    void doExport(Fact fact);

    void writeFile(String filePath);

    byte[] writeByte();

    void setUniqueId(String id);

    String getFileName();

}
