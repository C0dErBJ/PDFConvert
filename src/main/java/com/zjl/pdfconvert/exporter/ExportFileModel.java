package com.zjl.pdfconvert.exporter;

import org.springframework.http.MediaType;

/**
 * @author Zhu jialiang
 * @date 2020/9/1
 */
public class ExportFileModel {
    private String fileName;
    private String filePath;
    private MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;


    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
