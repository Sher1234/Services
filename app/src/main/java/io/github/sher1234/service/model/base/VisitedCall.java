package io.github.sher1234.service.model.base;

import java.util.Date;

public class VisitedCall extends Visit {

    private String Name;
    private String Phone;
    private String Email;
    private String EmployeeID;

    public VisitedCall() {

    }

    public VisitedCall(Date dateTime, String userID, String callNumber, String concernName,
                 String concernPhone, boolean isCompleted, int visit, String image, String feedback,
                 String location, String signature, String visitNumber, String actionTaken,
                 String observation, String concernEmail, String customerSatisfaction, String name,
                 String phone, String email, String employeeID) {
        super(dateTime, dateTime, userID, callNumber, concernName, concernPhone, isCompleted, visit, image,
                feedback, location, signature, visitNumber, actionTaken, observation, concernEmail,
                customerSatisfaction);
        Name = name;
        Phone = phone;
        Email = email;
        EmployeeID = employeeID;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }
}
