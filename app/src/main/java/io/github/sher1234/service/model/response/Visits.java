package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.Visit;

public class Visits implements Serializable {

    private Responded Response;
    private List<Visit> Visits;

    public Visits() {

    }

    public List<Visit> getVisits() {
        return Visits;
    }

    public Responded getResponse() {
        return Response;
    }
}