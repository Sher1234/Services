package io.github.sher1234.service.model.response;

import java.io.Serializable;
import java.util.List;

import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Response;
import io.github.sher1234.service.model.base.Time;

@SuppressWarnings("All")
public class DashboardWR extends Dashboard implements Serializable {

    private List<Registration> Registrations;

    public DashboardWR() {

    }

    public DashboardWR(Response response, Time regsMonthly, Time visitsMonthly,
                       Time regsWeekly, Time visitsWeekly, String pendingAll, int pendingWithVisit,
                       List<String> weekDates, List<String> quarterDates, List<Registration> registrations) {
        super(response, regsMonthly, visitsMonthly, regsWeekly, visitsWeekly, pendingAll,
                pendingWithVisit, weekDates, quarterDates);
        Registrations = registrations;
    }

    public List<Registration> getRegistrations() {
        return Registrations;
    }
}