package io.github.sher1234.service.model.response;

import java.io.Serializable;

import io.github.sher1234.service.model.base.User;

public class UserR implements Serializable {

    public int Code;
    public User User;
    public String Message;

    public UserR() {

    }
}
