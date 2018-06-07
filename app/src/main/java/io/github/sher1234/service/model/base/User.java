package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    String Name;
    String Email;
    String Phone;
    String UserID;
    String Password;
    String EmployeeID;

    public User() {

    }

    public User(String userID, String name, String phone, String email, String password, String employeeID) {
        UserID = userID;
        Name = name;
        Phone = phone;
        Email = email;
        Password = password;
        EmployeeID = employeeID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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

    public Map<String, String> getUserMap() {
        Map<String, String> map = new HashMap<>();
        map.put("UserID", UserID);
        map.put("Phone", Phone);
        map.put("Email", Email);
        map.put("Name", Name);
        map.put("Password", Password);
        map.put("EmployeeID", EmployeeID);
        return map;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isExists() {
        return  getName() != null && getName().length() > 0
                && getEmail() != null && getEmail().length() > 0
                && getUserID() != null && getUserID().length() > 0
                && getPassword() != null && getPassword().length() > 0
                && getEmployeeID() != null && getEmployeeID().length() > 0
                && getPhone() != null && getPhone().length() > 0;
    }
}