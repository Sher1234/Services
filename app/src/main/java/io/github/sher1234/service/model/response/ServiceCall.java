package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.RegisteredCall;
import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.VisitedCall;

public class ServiceCall implements Serializable {

    private Response Response;
    private List<VisitedCall> Visits;
    private List<RegisteredCall> Registrations;

    public ServiceCall() {

    }

    public ServiceCall(Response response, List<VisitedCall> visits,
                       List<RegisteredCall> registrations) {
        Response = response;
        this.Visits = visits;
        Registrations = registrations;
    }

    public List<RegisteredCall> getRegistrations() {
        return Registrations;
    }

    public void setRegistrations(List<RegisteredCall> registrations) {
        Registrations = registrations;
    }

    public Response getResponse() {
        return Response;
    }

    public void setResponse(Response response) {
        Response = response;
    }

    public List<VisitedCall> getVisits() {
        return Visits;
    }

    public void setVisits(List<VisitedCall> visits) {
        this.Visits = visits;
    }
}
