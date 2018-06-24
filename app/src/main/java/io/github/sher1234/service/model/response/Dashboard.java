package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.Time;

@SuppressWarnings("All")
public class Dashboard implements Serializable {

    private Response Response;

    private Time RegsDaily;

    private Time RegsMonthly;

    private Time VisitsDaily;

    private Time VisitsMonthly;

    private String PendingAll;

    private int PendingWithVisit;

    private List<String> DailyDates;

    private List<String> MonthlyDates;

    public Dashboard() {

    }

    public Dashboard(Response response, Time regsMonthly, Time visitsMonthly,
                     Time regsWeekly, Time visitsWeekly, String pendingAll, int pendingWithVisit,
                     List<String> weekDates, List<String> quarterDates) {
        Response = response;
        RegsMonthly = regsMonthly;
        VisitsMonthly = visitsMonthly;
        RegsDaily = regsWeekly;
        VisitsDaily = visitsWeekly;
        PendingAll = pendingAll;
        PendingWithVisit = pendingWithVisit;
        DailyDates = weekDates;
        MonthlyDates = quarterDates;
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

    public Time getRegsDaily() {
        return RegsDaily;
    }

    public Time getVisitsDaily() {
        return VisitsDaily;
    }

    public String getPendingAll() {
        return PendingAll;
    }

    public int getPendingWithVisit() {
        return PendingWithVisit;
    }

    public List<String> getDailyDates() {
        return DailyDates;
    }

    public List<String> getMonthlyDates() {
        return MonthlyDates;
    }
}