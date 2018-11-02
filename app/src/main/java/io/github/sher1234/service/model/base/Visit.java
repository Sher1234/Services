package io.github.sher1234.service.model.base;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import io.github.sher1234.service.util.Strings;

public class Visit implements Serializable {

    public String Name;
    public String Email;
    public String Phone;
    public String CallID;
    public String Action;
    public String VisitID;
    public String Location;
    public int Satisfaction;
    public String Signature;
    public String Feedback;
    private String ID;
    private String End;
    private String Start;
    private int IsCompleted;
    public String Observation;

    public Visit() {

    }

    public Visit(String id, String name, Date end, String email, String phone, String callID,
                 Date start, String action, String visitID, String feedback, String location,
                 String observation, int satisfaction, int isCompleted) {
        Satisfaction = satisfaction;
        Start = setDateTime(start);
        Observation = observation;
        IsCompleted = isCompleted;
        End = setDateTime(end);
        Location = location;
        Feedback = feedback;
        VisitID = visitID;
        Action = action;
        CallID = callID;
        Email = email;
        Phone = phone;
        Name = name;
        ID = id;
    }

    public Visit(String visitID, String callID, Date start, String id, String location) {
        Start = setDateTime(start);
        Location = location;
        VisitID = visitID;
        CallID = callID;
        ID = id;
    }

    public Visit(String visitID, String callID, String action, String observation,
                 String name, String phone, String email, int satisfaction,
                 Date end, String feedback, int isCompleted) {
        Satisfaction = satisfaction;
        Observation = observation;
        IsCompleted = isCompleted;
        End = setDateTime(end);
        Feedback = feedback;
        VisitID = visitID;
        Action = action;
        CallID = callID;
        Email = email;
        Phone = phone;
        Name = name;
    }

    public Visit(String visitID, String name, String phone, String email, String observation,
                 String action, int satisfaction, String feedback) {
        Satisfaction = satisfaction;
        Observation = observation;
        Feedback = feedback;
        VisitID = visitID;
        Action = action;
        Email = email;
        Phone = phone;
        Name = name;
    }

    private String setDateTime(Date date) {
        return getDateFormat().format(date);
    }

    public String getStartTimeView() {
        return getTimeFormatView().format(getStart());
    }

    public String getStartDateView() {
        return getDateFormatView().format(getStart());
    }

    public String getEndTimeView() {
        return getTimeFormatView().format(getEnd());
    }

    public String getSatisfaction() {
        return Satisfaction > 3 ? "Good" : Satisfaction > 2 ? "Average" : "Poor";
    }

    public Date getStart() {
        try {
            return getDateFormat().parse(Start);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setStart(Date date) {
        Start = setDateTime(date);
    }

    public void setStartTime(Date startTime) {
        Start = setDateTime(startTime);
    }

    public String getStartServer() {
        return Start;
    }

    public Date getEnd() {
        try {
            return getDateFormat().parse(End);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEnd(Date date) {
        End = setDateTime(date);
    }

    public void setEndTime(Date endTime) {
        End = setDateTime(endTime);
    }

    public String getEndServer() {
        return End;
    }

    public boolean isIncomplete() {
        return Start != null && (End == null || End.length() < 2);
    }

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Satisfaction", Satisfaction + "");
        map.put("IsCompleted", IsCompleted + "");
        map.put("Observation", Observation);
        map.put("Start", getStartServer());
        map.put("End", getEndServer());
        map.put("Location", Location);
        map.put("Feedback", Feedback);
        map.put("VisitID", VisitID);
        map.put("CallID", CallID);
        map.put("Action", Action);
        map.put("Email", Email);
        map.put("Phone", Phone);
        map.put("Name", Name);
        map.put("ID", ID);
        return map;
    }

    public Map<String, String> getEditMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Satisfaction", Satisfaction + "");
        map.put("Observation", Observation);
        map.put("Feedback", Feedback);
        map.put("VisitID", VisitID);
        map.put("Action", Action);
        map.put("Email", Email);
        map.put("Phone", Phone);
        map.put("Name", Name);
        return map;
    }

    public Map<String, String> getStartMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Start", getStartServer());
        map.put("Location", Location);
        map.put("VisitID", VisitID);
        map.put("CallID", CallID);
        map.put("ID", ID);
        return map;
    }

    public Map<String, String> getEndMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Satisfaction", Satisfaction + "");
        map.put("IsCompleted", IsCompleted + "");
        map.put("Observation", Observation);
        map.put("End", getEndServer());
        map.put("Feedback", Feedback);
        map.put("VisitID", VisitID);
        map.put("CallID", CallID);
        map.put("Action", Action);
        map.put("Email", Email);
        map.put("Phone", Phone);
        map.put("Name", Name);
        return map;
    }

    @NotNull
    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(Strings.DateTimeServer, Locale.US);
    }

    @NotNull
    private SimpleDateFormat getTimeFormatView() {
        return new SimpleDateFormat(Strings.DateTimeView, Locale.US);
    }

    @NotNull
    private SimpleDateFormat getDateFormatView() {
        return new SimpleDateFormat(Strings.DateView, Locale.US);
    }

    @NonNull
    @Override
    public String toString() {
        return "Satisfaction: " + Satisfaction + "\n" +
                "IsCompleted: " + IsCompleted + "\n" +
                "Observation: " + Observation + "\n" +
                "Start: " + getStartServer() + "\n" +
                "End: " + getEndServer() + "\n" +
                "Location: " + Location + "\n" +
                "Feedback: " + Feedback + "\n" +
                "VisitID: " + VisitID + "\n" +
                "CallID: " + CallID + "\n" +
                "Action: " + Action + "\n" +
                "Email: " + Email + "\n" +
                "Phone: " + Phone + "\n" +
                "Name: " + Name + "\n" +
                "ID: " + ID;
    }
}