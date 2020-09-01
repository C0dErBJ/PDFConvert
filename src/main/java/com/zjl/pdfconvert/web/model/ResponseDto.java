package com.zjl.pdfconvert.web.model;

/**
 * @author Zhu jialiang
 * @date 2020/9/1
 */
public class ResponseDto {
    private ResultCode resultCode;
    private String message;
    private String data;

    public static ResponseDto success() {
        ResponseDto dto = new ResponseDto();
        dto.setResultCode(ResultCode.SUCCESS);
        return dto;
    }

    public static ResponseDto fail() {
        ResponseDto dto = new ResponseDto();
        dto.setResultCode(ResultCode.FAIL);
        return dto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
