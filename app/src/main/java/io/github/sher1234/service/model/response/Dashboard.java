package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Quarterly;
import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.Weekly;

public class Dashboard implements Serializable {

    private Response Response;

    private Weekly RegsWeekly;

    private Quarterly RegsQuarterly;

    private Weekly VisitsWeekly;

    private Quarterly VisitsQuarterly;

    private String PendingAll;

    private int PendingWithVisit;

    private List<String> WeekDates;

    private List<String> QuarterDates;

    public Dashboard() {

    }

    public Dashboard(Response response, Quarterly regsQuarterly, Quarterly visitsQuarterly,
                     Weekly regsWeekly, Weekly visitsWeekly, String pendingAll, int pendingWithVisit,
                     List<String> weekDates, List<String> quarterDates) {
        Response = response;
        RegsQuarterly = regsQuarterly;
        VisitsQuarterly = visitsQuarterly;
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

    public Quarterly getRegsQuarterly() {
        return RegsQuarterly;
    }

    public Quarterly getVisitsQuarterly() {
        return VisitsQuarterly;
    }

    public Weekly getRegsWeekly() {
        return RegsWeekly;
    }

    public Weekly getVisitsWeekly() {
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