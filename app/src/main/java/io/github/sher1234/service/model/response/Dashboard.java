package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Time;
import io.github.sher1234.service.model.base.User;

@SuppressWarnings("All")
public class Dashboard implements Serializable {

    public int code;
    public User user;
    public Time rDaily;
    public Time vDaily;
    public String message;
    public Time rMonthly;
    public Time vMonthly;
    public String pending;
    public List<String> dDates;
    public List<String> mDates;
    public List<Registration> pRegs;


    public Dashboard() {

    }
}