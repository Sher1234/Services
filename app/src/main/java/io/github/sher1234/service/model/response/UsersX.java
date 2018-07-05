package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.UserX;

public class UsersX implements Serializable {

    private Responded Response;
    private List<UserX> Users;

    public UsersX() {

    }

    public List<UserX> getUsers() {
        return Users;
    }

    public Responded getResponse() {
        return Response;
    }
}