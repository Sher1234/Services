package io.github.sher1234.service.model.base;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.sher1234.service.util.Strings;

public class Call implements Serializable {

    public String ID1;
    public String ID2;
    public String CallID;
    private String DateTime;
    public String Warranty;
    public String ConcernName;
    public String ConcernPhone;
    public String SiteDetails;
    public String ProductDetail;
    public String CustomerName;
    public String NatureOfSite;
    public String ProductNumber;
    private int IsCompleted;
    public String ComplaintType;

    public Call() {

    }

    public Call(String id1, String id2, String callID, Date dateTime, String warranty,
                boolean isCompleted, String concernName, String siteDetails,
                String concernPhone, String customerName, String natureOfSite,
                String productNumber, String productDetail, String complaintType) {
        IsCompleted = setCompleted(isCompleted);
        DateTime = setDateTime(dateTime);
        ComplaintType = complaintType;
        ProductDetail = productDetail;
        ProductNumber = productNumber;
        ConcernPhone = concernPhone;
        CustomerName = customerName;
        NatureOfSite = natureOfSite;
        ConcernName = concernName;
        SiteDetails = siteDetails;
        Warranty = warranty;
        CallID = callID;
        ID2 = id2;
        ID1 = id1;
    }

    public Call(boolean isCompleted, String callID, String warranty, String concernName,
                String siteDetails, String concernPhone, String customerName,
                String natureOfSite, String productNumber, String productDetail,
                String complaintType) {
        IsCompleted = setCompleted(isCompleted);
        ComplaintType = complaintType;
        ProductDetail = productDetail;
        ProductNumber = productNumber;
        ConcernPhone = concernPhone;
        CustomerName = customerName;
        NatureOfSite = natureOfSite;
        ConcernName = concernName;
        SiteDetails = siteDetails;
        Warranty = warranty;
        CallID = callID;
    }

    @NotNull
    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(Strings.DateTimeServer, Locale.US);
    }

    @NotNull
    private SimpleDateFormat getDateFormatView() {
        return new SimpleDateFormat(Strings.DateTimeView, Locale.US);
    }

    public boolean isCompleted() {
        return IsCompleted == 1;
    }

    public String getIsCompleted() {
        return IsCompleted + "";
    }

    public String getIsCompletedString() {
        return IsCompleted == 1 ? "Closed/Completed" : "Opened/Pending";
    }

    public int setCompleted(boolean completed) {
        IsCompleted = completed ? 1 : 0;
        return IsCompleted;
    }

    public String setDateTime(Date date) {
        DateTime = getDateFormat().format(date);
        return DateTime;
    }

    public String getDateTimeView() {
        return getDateFormatView().format(getDateTime());
    }

    public Date getDateTime() {
        try {
            return getDateFormat().parse(DateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateTimeServer() {
        return DateTime;
    }

    public Map<String, String> getAddMap() {
        Map<String, String> map = getEditMap();
        map.put("DateTime", getDateTimeServer());
        map.put("ID1", ID1);
        return map;
    }

    public Map<String, String> getMap() {
        Map<String, String> map = getAddMap();
        map.put("ID2", ID2);
        return map;
    }

    public Map<String, String> getEditMap() {
        Map<String, String> map = new HashMap<>();
        map.put("CallID", CallID);
        map.put("Warranty", Warranty);
        map.put("ConcernName", ConcernName);
        map.put("SiteDetails", SiteDetails);
        map.put("CustomerName", CustomerName);
        map.put("NatureOfSite", NatureOfSite);
        map.put("ConcernPhone", ConcernPhone);
        map.put("ComplaintType", ComplaintType);
        map.put("ProductDetail", ProductDetail);
        map.put("ProductNumber", ProductNumber);
        map.put("IsCompleted", getIsCompleted());
        return map;
    }
}