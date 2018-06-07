package io.github.sher1234.service.model.base;

import android.annotation.SuppressLint;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("all")
public class Base implements Serializable {

    private String UserID;
    private int IsCompleted;
    private String DateTime;
    private String CallNumber;
    private String ConcernName;
    private String ConcernPhone;

    public Base() {

    }

    Base(Date date, String userID, String callNumber, String concernName,
                String concernPhone, boolean isCompleted) {
        UserID = userID;
        CallNumber = callNumber;
        DateTime = setDateTime(date);
        ConcernName = concernName;
        IsCompleted = setCompleted(isCompleted);
        ConcernPhone = concernPhone;
    }

    @NotNull
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    private String setDateTime(Date date) {
        return  getDateFormat().format(date);
    }

    public Date getDateTime() {
        try {
            return getDateFormat().parse(DateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateTimeString() {
        return DateTime;
    }

    public boolean isCompleted() {
        return IsCompleted == 1;
    }

    public String getIsCompleted() {
        return IsCompleted + "";
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getConcernName() {
        return ConcernName;
    }

    public void setConcernName(String concernName) {
        ConcernName = concernName;
    }
}
