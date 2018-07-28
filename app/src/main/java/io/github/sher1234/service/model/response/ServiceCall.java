package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.VisitX;

public class ServiceCall implements Serializable {

    public int code;
    public String message;
    public User allottedTo;
    public User registeredBy;
    public List<VisitX> visits;
    public Registration registration;

    public ServiceCall() {

    }
}