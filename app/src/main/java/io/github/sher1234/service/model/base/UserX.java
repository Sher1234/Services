package io.github.sher1234.service.model.base;

import java.io.Serializable;

public class UserX implements Serializable {

    private int Regs;
    private int Visits;
    private int Pending;
    private String Name;
    private String Email;
    private String Phone;
    private String EmployeeID;

    public UserX() {

    }

    public UserX(int regs, int visits, int pending, String name, String phone, String email,
                 String employeeID) {
        Regs = regs;
        Visits = visits;
        Pending = pending;
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

    public int getRegs() {
        return Regs;
    }

    public int getVisits() {
        return Visits;
    }

    public int getPending() {
        return Pending;
    }
}