package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.User;

public class Users implements Serializable {

    private Responded Response;
    private List<User> Users;

    public Users() {

    }

    public List<User> getUsers() {
        return Users;
    }

    public Responded getResponse() {
        return Response;
    }
}
