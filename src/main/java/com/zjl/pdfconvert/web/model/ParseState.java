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
public enum ParseState implements JSONSerializable {
    PARSING(1), DONE(2);
    private int value;

    ParseState(int value) {
        this.value = value;
    }
    @JsonValue
    public int getValue() {
        return value;
    }

    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Type type, int i) throws IOException {
        JSONObject object = new JSONObject();
        object.put("parseState", value);
        jsonSerializer.write(object);
    }
}
