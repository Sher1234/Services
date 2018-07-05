package io.github.sher1234.service.model.response;

import java.io.Serializable;

public class Responded implements Serializable {

    private io.github.sher1234.service.model.base.Responded Response;

    public Responded() {

    }

    public io.github.sher1234.service.model.base.Responded getResponse() {
        return Response;
    }
}