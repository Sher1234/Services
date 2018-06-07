package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Visit extends Base implements Serializable {

    private int Visit;
    private String Image;
    private String Feedback;
    private String Location;
    private String Signature;
    private String VisitNumber;
    private String ActionTaken;
    private String Observation;
    private String ConcernEmail;
    private String CustomerSatisfaction;

    public Visit() {

    }

    public Visit(Date dateTime, String userID, String callNumber, String concernName,
                 String concernPhone, boolean isCompleted, int visit, String image, String feedback,
                 String location, String signature, String visitNumber, String actionTaken,
                 String observation, String concernEmail, String customerSatisfaction) {
        super(dateTime, userID, callNumber, concernName, concernPhone, isCompleted);
        Visit = visit;
        Image = image;
        Feedback = feedback;
        Location = location;
        Signature = signature;
        VisitNumber = visitNumber;
        ActionTaken = actionTaken;
        Observation = observation;
        ConcernEmail = concernEmail;
        CustomerSatisfaction = customerSatisfaction;
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

    public Map<String, String> getVisitMap() {
        Map<String, String> map = new HashMap<>();
        map.put("UserID", getUserID());
        map.put("CallNumber", getCallNumber());
        map.put("DateTime", getDateTimeString());
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
}
