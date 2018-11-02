package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.User;

@SuppressWarnings("All")
public class Dashboard implements Serializable {

    public int code;
    public User user;
    public String tCalls;
    public String pCalls;
    public String tVisits;
    public String message;
    public List<Call> calls;
    public List<String> days;
    public List<String> months;
    public List<Integer> dCalls;
    public List<Integer> mCalls;
    public List<Integer> dVisits;
    public List<Integer> mVisits;

    public Dashboard() {

    }
}