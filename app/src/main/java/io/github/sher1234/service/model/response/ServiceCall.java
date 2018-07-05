package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.RegisteredCall;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.VisitImage;
import io.github.sher1234.service.model.base.VisitedCall;

public class ServiceCall implements Serializable {

    private Responded Response;
    private List<VisitedCall> Visits;
    private RegisteredCall Registration;
    private List<VisitImage> VisitImages;

    public ServiceCall() {

    }

    public ServiceCall(Responded response, List<VisitedCall> visits,
                       RegisteredCall registrations, List<VisitImage> visitImages) {
        Response = response;
        this.Visits = visits;
        VisitImages = visitImages;
        Registration = registrations;
    }

    public RegisteredCall getRegistration() {
        return Registration;
    }

    public Responded getResponse() {
        return Response;
    }

    public List<VisitedCall> getVisits() {
        return Visits;
    }

    public List<VisitImage> getVisitImages() {
        return VisitImages;
    }
}