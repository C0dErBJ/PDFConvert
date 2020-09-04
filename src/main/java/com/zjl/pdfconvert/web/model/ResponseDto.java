package com.zjl.pdfconvert.web.model;

/**
 * @author Zhu jialiang
 * @date 2020/9/1
 */
public class ResponseDto<T> {
    private ResultCode resultCode;
    private String message;
    private T data;

    public static <T> ResponseDto<T> success() {
        ResponseDto<T> dto = new ResponseDto<>();
        dto.setResultCode(ResultCode.SUCCESS);
        return dto;
    }

    public static <T> ResponseDto<T> fail() {
        ResponseDto<T> dto = new ResponseDto<>();
        dto.setResultCode(ResultCode.FAIL);
        return dto;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
