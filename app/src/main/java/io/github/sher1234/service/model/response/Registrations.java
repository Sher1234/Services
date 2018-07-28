package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Registration;

public class Registrations implements Serializable {

    public int Code;
    public String Message;
    public List<Registration> Registrations;

    public Registrations() {

    }
}
