package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Responded;

public class Registrations implements Serializable {

    private Responded Response;
    private List<Registration> Registrations;

    public Registrations() {

    }

    public Registrations(Responded response, List<Registration> registrations) {
        Response = response;
        Registrations = registrations;
    }

    public List<Registration> getRegistrations() {
        return Registrations;
    }

    public Responded getResponse() {
        return Response;
    }
}
