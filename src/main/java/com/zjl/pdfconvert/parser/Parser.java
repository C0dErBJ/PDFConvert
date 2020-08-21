package com.zjl.pdfconvert.parser;

import com.zjl.pdfconvert.model.Fact;

import java.io.File;
import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public interface Parser {

    List<Fact> parse();

    void setFilePath(String filePath);
}
