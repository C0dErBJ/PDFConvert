package com.zjl.pdfconvert.web.model;

/**
 * @author Zhu jialiang
 * @date 2020/9/1
 */
public enum ResultCode {
    SUCCESS(1), FAIL(0);
    private int value;

    ResultCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
