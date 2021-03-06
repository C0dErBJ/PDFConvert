package com.zjl.pdfconvert.web.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Zhu jialiang
 * @date 2020/9/1
 */
public enum ResultCode implements JSONSerializable {
    SUCCESS(1), FAIL(0);
    private int value;

    ResultCode(int value) {
        this.value = value;
    }
    @JsonValue
    public int getValue() {
        return value;
    }

    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Type type, int i) throws IOException {
        JSONObject object = new JSONObject();
        object.put("resultCode", value);
        jsonSerializer.write(object);
    }
}
