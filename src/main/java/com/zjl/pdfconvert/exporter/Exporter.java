package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Fact;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public interface Exporter {

    void export(Fact fact);

    void setFilePath(String filePath);

    void writeFile();

}
