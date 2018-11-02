package io.github.sher1234.service.model.base;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.sher1234.service.util.Strings;

public class User implements Serializable {

    public String Name;
    public String Email;
    public String Phone;
    public int IsAdmin;
    public String UserID;
    public String Password;
    public int IsRegistered;
    public String EmployeeID;

    private String Recent;

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
        IsAdmin = 0;
        if (isAdmin)
            IsAdmin = 1;
    }

    public String getRecent() {
        if (Recent == null || Recent.isEmpty()) return "None";
        try {
            DateFormat dateFormat1 = new SimpleDateFormat(Strings.DateTimeServer, Locale.US);
            DateFormat dateFormat2 = new SimpleDateFormat(Strings.DateTimeView, Locale.US);
            return dateFormat2.format(dateFormat1.parse(Recent));
        } catch (ParseException e) {
            e.printStackTrace();
            return Recent;
        }
    }

    public boolean isAdmin() {
        return IsAdmin == 1;
    }

    public void setRegistered(boolean isRegistered) {
        IsRegistered = 0;
        if (isRegistered)
            IsRegistered = 1;
    }

    public boolean isRegistered() {
        return IsRegistered == 1;
    }

    public Map<String, String> getMap() {
        Map<String, String> map = getEditMap();
        map.put("IsRegistered", IsRegistered + "");
        map.put("IsAdmin", IsAdmin + "");
        return map;
    }

    public Map<String, String> getEditMap() {
        Map<String, String> map = getRegister();
        map.put("UserID", UserID);
        return map;
    }

    public Map<String, String> getRegister() {
        Map<String, String> map = new HashMap<>();
        map.put("EmployeeID", EmployeeID);
        map.put("Password", Password);
        map.put("Phone", Phone);
        map.put("Email", Email);
        map.put("Name", Name);
        return map;
    }

    public boolean isExists() {
        return Name != null && Name.length() > 2
                && Email != null && Email.length() > 4
                && Phone != null && Phone.length() == 10
                && Password != null && Password.length() > 7
                && Email.contains("@") && Email.contains(".")
                && EmployeeID != null && EmployeeID.length() > 9;
    }

    @NotNull
    @Override
    public String toString() {
        return "Name: " + Name + "\n" +
                "Phone: " + Phone + "\n" +
                "Email: " + Email + "\n" +
                "UserID: " + UserID + "\n" +
                "IsAdmin: " + IsAdmin + "\n" +
                "Password: " + Password + "\n" +
                "EmployeeID: " + EmployeeID + "\n" +
                "IsRegistered: " + IsRegistered + "\n";
    }
}