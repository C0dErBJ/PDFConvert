package com.zjl.pdfconvert.util;

/**
 * @author Zhu jialiang
 * @date 2020/9/1
 */
public class FileUtil {
    public static String trimExt(String filename) {
        String[] filenameExt = filename.split("\\.");
        if (filenameExt.length < 2) {
            return filename;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filenameExt.length - 1; i++) {
            sb.append(filenameExt[i]);
        }
        return sb.toString();
    }
}
