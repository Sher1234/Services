package io.github.sher1234.service.model.base;

import java.io.Serializable;

public class Responded implements Serializable {

    private String Message;
    private int Code;

    public Responded() {

    }

    public String getMessage() {
        return Message;
    }

    public int getCode() {
        return Code;
    }
}