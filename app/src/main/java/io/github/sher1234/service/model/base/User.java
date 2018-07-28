package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private int IsAdmin;
    public String Name;
    public String Email;
    public String Phone;
    public String JoinDate;
    public String Password;
    private int IsRegistered;
    public String EmployeeID;

    public User() {

    }

    public User(boolean isAdmin, String name, String phone, String email,
                String password, String employeeID) {
        setAdmin(isAdmin);
        Name = name;
        Phone = phone;
        Email = email;
        Password = password;
        EmployeeID = employeeID;
    }

    public void setAdmin(boolean isAdmin) {
        if (isAdmin)
            IsAdmin = 1;
        else
            IsAdmin = 0;
    }

    public boolean isAdmin() {
        return IsAdmin == 1;
    }

    public void setRegistered(boolean isRegistered) {
        if (isRegistered)
            IsRegistered = 1;
        else
            IsRegistered = 0;
    }

    public boolean isRegistered() {
        return IsRegistered == 1;
    }

    public Map<String, String> getUserMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Name", Name);
        map.put("Email", Email);
        map.put("Phone", Phone);
        map.put("Password", Password);
        map.put("EmployeeID", EmployeeID);
        map.put("IsAdmin", isAdmin() + "");
        map.put("IsRegistered", isRegistered() + "");
        return map;
    }

    public boolean isExists() {
        return Name != null && Name.length() > 1
                && Email != null && Email.length() > 3
                && Phone != null && Phone.length() == 10
                && Password != null && Password.length() > 7
                && EmployeeID != null && EmployeeID.length() > 9;
    }
}