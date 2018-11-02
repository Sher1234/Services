package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.Visits;

public class Services implements Serializable {

    public int code;
    public Call call;
    public User user1;
    public User user2;
    public String message;
    public List<Visits> visits;

    public Services() {

    }
}