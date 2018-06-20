package io.github.sher1234.service.model.base;

import java.util.Date;

@SuppressWarnings("All")
public class RegisteredCall extends Registration {

    private String Name;
    private String Phone;
    private String EmployeeID;

    public RegisteredCall() {

    }

    public RegisteredCall(int numberOfVisits, String siteDetails, String customerName,
                          String natureOfSite, String productDetail, String productNumber,
                          String complaintType, String warrantyStatus, Date dateTime,
                          String callNumber, String concernName, String concernPhone,
                          boolean isCompleted, String name, String phone, String email,
                          String employeeID) {
        super(numberOfVisits, siteDetails, customerName, natureOfSite, productDetail, productNumber,
                complaintType, warrantyStatus, dateTime, email, callNumber, concernName,
                concernPhone, isCompleted);
        Name = name;
        Phone = phone;
        EmployeeID = employeeID;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }
}
