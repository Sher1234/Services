package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.Time;

@SuppressWarnings("All")
public class Dashboard implements Serializable {

    private Response Response;

    private Time RegsWeekly;

    private Time RegsMonthly;

    private Time VisitsWeekly;

    private Time VisitsMonthly;

    private String PendingAll;

    private int PendingWithVisit;

    private List<String> WeekDates;

    private List<String> QuarterDates;

    public Dashboard() {

    }

    public Dashboard(Response response, Time regsMonthly, Time visitsMonthly,
                     Time regsWeekly, Time visitsWeekly, String pendingAll, int pendingWithVisit,
                     List<String> weekDates, List<String> quarterDates) {
        Response = response;
        RegsMonthly = regsMonthly;
        VisitsMonthly = visitsMonthly;
        RegsWeekly = regsWeekly;
        VisitsWeekly = visitsWeekly;
        PendingAll = pendingAll;
        PendingWithVisit = pendingWithVisit;
        WeekDates = weekDates;
        QuarterDates = quarterDates;
    }

    public Response getResponse() {
        return Response;
    }

    public Time getRegsMonthly() {
        return RegsMonthly;
    }

    public Time getVisitsMonthly() {
        return VisitsMonthly;
    }

    public Time getRegsWeekly() {
        return RegsWeekly;
    }

    public Time getVisitsWeekly() {
        return VisitsWeekly;
    }

    public String getPendingAll() {
        return PendingAll;
    }

    public int getPendingWithVisit() {
        return PendingWithVisit;
    }

    public List<String> getWeekDates() {
        return WeekDates;
    }

    public List<String> getQuarterDates() {
        return QuarterDates;
    }
}