package io.github.sher1234.service.model.response;

import java.io.Serializable;

public class Responded implements Serializable {

    private io.github.sher1234.service.model.base.Response Response;

    public Responded() {

    }

    public Responded(io.github.sher1234.service.model.base.Response response) {
        Response = response;
    }

    public io.github.sher1234.service.model.base.Response getResponse() {
        return Response;
    }

    public void setResponse(io.github.sher1234.service.model.base.Response response) {
        Response = response;
    }
}
