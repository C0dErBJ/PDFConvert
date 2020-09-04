package com.zjl.pdfconvert.web;

import com.zjl.pdfconvert.executor.AsyncExecutor;
import com.zjl.pdfconvert.exporter.ExportFileModel;
import com.zjl.pdfconvert.exporter.WordExporter;
import com.zjl.pdfconvert.parser.PdfParser;
import com.zjl.pdfconvert.util.FileUtil;
import com.zjl.pdfconvert.web.model.FileType;
import com.zjl.pdfconvert.web.model.ResponseDto;
import com.zjl.pdfconvert.web.model.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author Zhu jialiang
 * @date 2020/8/18
 */
@RestController
@RequestMapping("/file")
public class FileController {

    final AsyncExecutor executor;

    @Autowired
    public FileController(AsyncExecutor executor) {
        this.executor = executor;
    }

    @PostMapping(value = "/word", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseDto<String>> parseWord(@RequestPart("file") FilePart filePart) {
        ResponseDto<String> result = ResponseDto.fail();
        if (!preCheck(filePart.filename())) {
            result.setMessage("请上传pdf文件");
            return Mono.just(result);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataBufferUtils.write(filePart.content(), outputStream).doOnComplete(() ->
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(outputStream.toByteArray());
            PdfParser parser = new PdfParser(bis);
            parser.setFileName(filePart.filename());
            WordExporter exporter = new WordExporter();
            exporter.setFileName(FileUtil.trimExt(filePart.filename()));
            result.setResultCode(ResultCode.SUCCESS);
            result.setMessage("上传成功");
            result.setData(this.executor.doExecutor(parser, exporter));
        }).subscribe();

        return Mono.just(result);
    }


    @GetMapping("/download/{uuid}")
    public Mono<Void> downloadFile(@PathVariable("uuid") String uuid, ServerHttpResponse response) {
        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        ExportFileModel fileModel = this.executor.getExportFile(uuid);
        response.getHeaders().setContentType(fileModel.getMediaType());
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileModel.getFileName());
        File file = new File(fileModel.getFilePath());
        return zeroCopyResponse.writeWith(file, 0, file.length());
    }

    private boolean preCheck(String filename) {
        String[] filenameExt = filename.split("\\.");
        if (filenameExt.length < 2) {
            return false;
        }
        return FileType.PDF.equalsIgnoreCase(filenameExt[filenameExt.length - 1]);
    }
}
