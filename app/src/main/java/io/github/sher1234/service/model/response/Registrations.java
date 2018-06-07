package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Response;

public class Registrations implements Serializable {

    private Response Response;
    private List<Registration> Registrations;

    public Registrations() {

    }

    public Registrations(Response response, List<Registration> registrations) {
        Response = response;
        Registrations = registrations;
    }

    public List<Registration> getRegistrations() {
        return Registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        Registrations = registrations;
    }

    public Response getResponse() {
        return Response;
    }

    public void setResponse(Response response) {
        Response = response;
    }
}
