package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.UserX;

public class UsersX implements Serializable {

    private Response Response;
    private List<UserX> Users;

    public UsersX() {

    }

    public UsersX(Response response, List<UserX> users) {
        Response = response;
        Users = users;
    }

    public List<UserX> getUsers() {
        return Users;
    }

    public Response getResponse() {
        return Response;
    }
}
