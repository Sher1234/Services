package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.User;

public class Users implements Serializable {

    public int Code;
    public String Message;
    public List<User> Users;

    public Users() {
    }
}
