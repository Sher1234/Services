package io.github.sher1234.service.model.response;

import java.io.Serializable;

import io.github.sher1234.service.model.base.Token;
import io.github.sher1234.service.model.base.User;

public class AccountJson implements Serializable {

    public int Code;
    public User User;
    public Token Token;
    public String Message;

    public AccountJson() {
    }
}
