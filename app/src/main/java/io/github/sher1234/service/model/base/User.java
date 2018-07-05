package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private int IsAdmin;
    private String Name;
    private String Email;
    private String Phone;
    private String Password;
    private int IsRegistered;
    private String EmployeeID;

    public User() {

    }

    public User(boolean isAdmin, String name, String phone, String email,
                String password, String employeeID) {
        IsAdmin = setAdmin(isAdmin);
        Name = name;
        Phone = phone;
        Email = email;
        Password = password;
        EmployeeID = employeeID;
    }

    public boolean isAdmin() {
        return IsAdmin == 1;
    }

    public boolean isRegistered() {
        return IsRegistered == 1;
    }

    public String getIsAdmin() {
        return IsAdmin + "";
    }

    public String getIsRegistered() {
        return IsRegistered + "";
    }

    public int setAdmin(boolean isAdmin) {
        if (isAdmin)
            return 1;
        return  0;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Map<String, String> getUserMap() {
        Map<String, String> map = new HashMap<>();
        map.put("IsAdmin", getIsAdmin());
        map.put("Phone", Phone);
        map.put("Email", Email);
        map.put("Name", Name);
        map.put("Password", Password);
        map.put("EmployeeID", EmployeeID);
        map.put("IsRegistered", getIsRegistered());
        return map;
    }
    public boolean isExists() {
        return  getName() != null && getName().length() > 0
                && getEmail() != null && getEmail().length() > 0
                && !getIsAdmin().isEmpty() && getIsAdmin().length() > 0
                && getPassword() != null && getPassword().length() > 0
                && getEmployeeID() != null && getEmployeeID().length() > 0
                && getPhone() != null && getPhone().length() > 0;
    }
}