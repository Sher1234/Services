package io.github.sher1234.service.model.base;

import java.io.Serializable;

public class Response implements Serializable {

    private String Message;
    private int Code;

    public Response() {

    }

    public Response(String message, int code) {
        this.Message = message;
        this.Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        this.Code = code;
    }
}