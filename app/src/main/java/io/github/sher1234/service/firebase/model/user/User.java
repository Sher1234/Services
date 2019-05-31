package io.github.sher1234.service.firebase.model.user;

import com.google.firebase.firestore.PropertyName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class User implements Serializable {

    @PropertyName("first_name") public String firstName;
    @PropertyName("last_name") public String lastName;
    @PropertyName("enabled") public boolean enabled;
    @PropertyName("admin") public boolean admin;
    @PropertyName("recent") public Date recent;
    @PropertyName("email") public String email;
    @PropertyName("phone") public Phone phone;
    @PropertyName("uid") public String uid;
    @PropertyName("eid") public String eid;

    public User() {}

    public boolean isValid() {
        return phone != null && phone.number.length() == 10 && firstName != null
                && firstName.length() > 2 && email != null && email.length() > 4
                && email.contains("@") && email.contains(".") && eid != null && eid.length() > 9;
    }

    @NotNull
    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + "\n" + "Phone: " + phone.toString() + "\n" +
                "Email: " + email + "\n" + "UserID: " + uid + "\n" + "IsAdmin: " + admin + "\n" +
                "IsRegistered: " + enabled + "\n" + "EmployeeID: " + eid + "\n" +
                "IsValid: " + isValid() + "\n" + "Recent: " + recent.getTime() + "\n";
    }

    public User signUp(String first, String last, String phone, String email, String eid, String code) {
        return edit(first, last, phone, email, eid, null, code, false, false);
    }

    public User edit(String first, String last, String phone, String email, String eid, String uid,
                     String code, boolean enabled, boolean admin) {
        this.recent = Calendar.getInstance().getTime();
        this.phone = new Phone(code, phone);
        this.enabled = enabled;
        this.firstName = first;
        this.lastName =  last;
        this.admin = admin;
        this.email = email;
        this.uid = uid;
        this.eid = eid;
        return this;
    }
}