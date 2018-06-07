package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.User;

public class Users implements Serializable {

    private Response Response;
    private List<User> Users;

    public Users() {

    }

    public Users(Response response, List<User> users) {
        Response = response;
        Users = users;
    }

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }

    public Response getResponse() {
        return Response;
    }

    public void setResponse(Response response) {
        Response = response;
    }
}
