package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.Visit;

public class Visits implements Serializable {

    private Response Response;
    private List<Visit> Visits;

    public Visits() {

    }

    public Visits(Response response, List<Visit> visits) {
        Response = response;
        Visits = visits;
    }

    public List<Visit> getVisits() {
        return Visits;
    }

    public void setVisits(List<Visit> users) {
        Visits = users;
    }

    public Response getResponse() {
        return Response;
    }

    public void setResponse(Response response) {
        Response = response;
    }
}