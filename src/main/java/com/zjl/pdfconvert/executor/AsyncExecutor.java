package com.zjl.pdfconvert.executor;

import com.zjl.pdfconvert.exporter.ExportFileModel;
import com.zjl.pdfconvert.exporter.Exporter;
import com.zjl.pdfconvert.parser.Parser;


/**
 * @author Zhu jialiang
 * @date 2020/8/31
 */
public interface AsyncExecutor extends Executor<String> {

    @Override
    String doExecutor(Parser parser, Exporter exporter);

    ExportFileModel getExportFile(String uuid);

    void onComplete(ParseCompleteListener callBack);


}
