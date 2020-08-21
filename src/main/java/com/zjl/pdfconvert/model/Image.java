package com.zjl.pdfconvert.model;

import java.io.InputStream;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Image extends Element {
    private String fileName;
    private int width;
    private int height;

    private float displayWidth;
    private float displayHeight;
    private byte[] file;


    public float getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(float displayWidth) {
        this.displayWidth = displayWidth;
    }

    public float getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(float displayHeight) {
        this.displayHeight = displayHeight;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
