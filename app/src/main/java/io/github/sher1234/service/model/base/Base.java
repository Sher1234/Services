package io.github.sher1234.service.model.base;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.github.sher1234.service.util.Strings;

@SuppressWarnings("all")
public class Base implements Serializable {

    public String Email;
    private int IsCompleted;
    public String CallNumber;
    public String ConcernName;
    public String ConcernPhone;

    Base() {

    }

    Base(String email, String callNumber, String concernName,
         String concernPhone, boolean isCompleted) {
        Email = email;
        CallNumber = callNumber;
        ConcernName = concernName;
        ConcernPhone = concernPhone;
        IsCompleted = setCompleted(isCompleted);
    }

    @NotNull
    protected SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(Strings.DateTimeServer, Locale.US);
    }

    @NotNull
    protected SimpleDateFormat getDateFormatView() {
        return new SimpleDateFormat(Strings.DateTimeView, Locale.US);
    }

    public boolean isCompleted() {
        return IsCompleted == 1;
    }

    public String getIsCompleted() {
        return IsCompleted + "";
    }

    public String getIsCompletedString() {
        if (IsCompleted == 1)
            return "Completed";
        else
            return "Pending";
    }

    public int setCompleted(boolean completed) {
        if (completed)
            return 1;
        return  0;
    }

    public String getConcernPhone() {
        return ConcernPhone;
    }

    public void setConcernPhone(String concernPhone) {
        ConcernPhone = concernPhone;
    }

    public String getCallNumber() {
        return CallNumber;
    }

    public void setCallNumber(String callNumber) {
        CallNumber = callNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getConcernName() {
        return ConcernName;
    }

    public void setConcernName(String concernName) {
        ConcernName = concernName;
    }
}
