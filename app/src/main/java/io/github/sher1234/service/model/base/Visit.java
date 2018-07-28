package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("All")
public class Visit extends Base implements Serializable {

    private int Visit;
    private String Image;
    private String EndTime;
    public String Feedback;
    private String Location;
    private String Signature;
    private String StartTime;
    public String VisitNumber;
    public String ActionTaken;
    public String Observation;
    public String ConcernEmail;
    public String CustomerSatisfaction;

    public Visit() {

    }

    public Visit(Date startTime, Date endTime, String email, String callNumber, String concernName,
                 String concernPhone, boolean isCompleted, int visit, String image, String feedback,
                 String location, String signature, String visitNumber, String actionTaken,
                 String observation, String concernEmail, String customerSatisfaction) {
        super(email, callNumber, concernName, concernPhone, isCompleted);
        Visit = visit;
        Image = image;
        Feedback = feedback;
        Location = location;
        Signature = signature;
        VisitNumber = visitNumber;
        ActionTaken = actionTaken;
        Observation = observation;
        ConcernEmail = concernEmail;
        EndTime = setDateTime(endTime);
        StartTime = setDateTime(startTime);
        CustomerSatisfaction = customerSatisfaction;
    }

    public Visit(String visitNumber, String callNumber, int visit, Date startTime,
                 String email, String location) {
        super(email, callNumber, null, null, false);
        Visit = visit;
        Location = location;
        VisitNumber = visitNumber;
        StartTime = setDateTime(startTime);
    }

    public Visit(String visitNumber, String callNumber, boolean isCompleted, String actionTaken,
                 String observation, String concernName, String concernPhone, String concernEmail,
                 String customerSatisfaction, Date endTime, String feedback, String image,
                 String signature) {
        super(null, callNumber, concernName, concernPhone, isCompleted);
        Image = image;
        Feedback = feedback;
        Signature = signature;
        VisitNumber = visitNumber;
        ActionTaken = actionTaken;
        Observation = observation;
        ConcernEmail = concernEmail;
        EndTime = setDateTime(endTime);
        CustomerSatisfaction = customerSatisfaction;
    }

    public Visit(String visitNumber, String concernName, String concernPhone, String concernEmail,
                 String observation, String actionTaken, String customerSatisfaction,
                 String feedback) {
        super(null, null, concernName, concernPhone, false);
        Feedback = feedback;
        VisitNumber = visitNumber;
        ActionTaken = actionTaken;
        Observation = observation;
        ConcernEmail = concernEmail;
        CustomerSatisfaction = customerSatisfaction;
    }

    private String setDateTime(Date date) {
        return getDateFormat().format(date);
    }

    public String getStartTimeView() {
        return getDateFormatView().format(getStartTime());
    }

    public String getEndTimeView() {
        return getDateFormatView().format(getEndTime());
    }

    public Date getStartTime() {
        try {
            return getDateFormat().parse(StartTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setStartTime(Date startTime) {
        StartTime = setDateTime(startTime);
    }

    public String getStartTimeString() {
        return StartTime;
    }

    public Date getEndTime() {
        try {
            return getDateFormat().parse(EndTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEndTime(Date endTime) {
        EndTime = setDateTime(endTime);
    }

    public String getEndTimeString() {
        return EndTime;
    }

    public String getCustomerSatisfaction() {
        return CustomerSatisfaction;
    }

    public void setCustomerSatisfaction(String customerSatisfaction) {
        CustomerSatisfaction = customerSatisfaction;
    }

    public String getConcernEmail() {
        return ConcernEmail;
    }

    public void setConcernEmail(String concernEmail) {
        ConcernEmail = concernEmail;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
    }

    public String getActionTaken() {
        return ActionTaken;
    }

    public void setActionTaken(String actionTaken) {
        ActionTaken = actionTaken;
    }

    public String getVisitNumber() {
        return VisitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        VisitNumber = visitNumber;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getVisit() {
        return Visit;
    }

    public String getVisitString() {
        return getVisit() + "";
    }

    public void setVisit(int visit) {
        Visit = visit;
    }

    public boolean isVisitStarted() {
        return StartTime != null && (EndTime == null || EndTime.length() < 2);
    }

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Email", getEmail());
        map.put("CallNumber", getCallNumber());
        map.put("EndTime", getEndTimeString());
        map.put("StartTime", getStartTimeString());
        map.put("ConcernName", getConcernName());
        map.put("IsCompleted", getIsCompleted());
        map.put("Visit", getVisitString());
        map.put("Image", getImage());
        map.put("Feedback", getFeedback());
        map.put("ConcernPhone", getConcernPhone());
        map.put("Location", getLocation());
        map.put("Signature", getSignature());
        map.put("VisitNumber", getVisitNumber());
        map.put("ActionTaken", getActionTaken());
        map.put("Observation", getObservation());
        map.put("ConcernEmail", getConcernEmail());
        map.put("CustomerSatisfaction", getCustomerSatisfaction());
        return map;
    }

    public Map<String, String> getEditMap() {
        Map<String, String> map = new HashMap<>();
        map.put("ConcernName", getConcernName());
        map.put("Feedback", getFeedback());
        map.put("ConcernPhone", getConcernPhone());
        map.put("VisitNumber", getVisitNumber());
        map.put("ActionTaken", getActionTaken());
        map.put("Observation", getObservation());
        map.put("ConcernEmail", getConcernEmail());
        map.put("CustomerSatisfaction", getCustomerSatisfaction());
        return map;
    }

    public Map<String, String> getStartMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Email", getEmail());
        map.put("CallNumber", getCallNumber());
        map.put("StartTime", getStartTimeString());
        map.put("Visit", getVisitString());
        map.put("Location", getLocation());
        map.put("VisitNumber", getVisitNumber());
        return map;
    }

    public Map<String, String> getEndMap() {
        Map<String, String> map = new HashMap<>();
        map.put("CallNumber", getCallNumber());
        map.put("EndTime", getEndTimeString());
        map.put("ConcernName", getConcernName());
        map.put("IsCompleted", getIsCompleted());
        map.put("Image", getImage());
        map.put("Feedback", getFeedback());
        map.put("ConcernPhone", getConcernPhone());
        map.put("Signature", getSignature());
        map.put("VisitNumber", getVisitNumber());
        map.put("ActionTaken", getActionTaken());
        map.put("Observation", getObservation());
        map.put("ConcernEmail", getConcernEmail());
        map.put("CustomerSatisfaction", getCustomerSatisfaction());
        return map;
    }

}
