package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class Registration extends Base implements Serializable {

    private String DateTime;
    public String AllottedTo;
    public int NumberOfVisits;
    public String SiteDetails;
    public String CustomerName;
    public String NatureOfSite;
    public String ProductDetail;
    public String ProductNumber;
    public String ComplaintType;
    public String WarrantyStatus;

    public Registration() {

    }

    public Registration(int numberOfVisits, String siteDetails, String customerName,
                        String natureOfSite, String productDetail, String productNumber,
                        String complaintType, String warrantyStatus, Date dateTime, String email,
                        String callNumber, String concernName, String concernPhone,
                        boolean isCompleted, String allottedTo) {
        super(email, callNumber, concernName, concernPhone, isCompleted);
        NumberOfVisits = numberOfVisits;
        SiteDetails = siteDetails;
        CustomerName = customerName;
        NatureOfSite = natureOfSite;
        ProductDetail = productDetail;
        ProductNumber = productNumber;
        ComplaintType = complaintType;
        WarrantyStatus = warrantyStatus;
        DateTime = setDateTime(dateTime);
        AllottedTo = allottedTo;
    }

    public Registration(String customerName, String siteDetails, String concernName,
                        String concernPhone, String productDetail, String productNumber,
                        String natureOfSite, String complaintType, String warrantyStatus,
                        boolean isCompleted, String callNumber) {
        super(null, callNumber, concernName, concernPhone, isCompleted);
        NumberOfVisits = 0;
        SiteDetails = siteDetails;
        CustomerName = customerName;
        NatureOfSite = natureOfSite;
        ProductDetail = productDetail;
        ProductNumber = productNumber;
        ComplaintType = complaintType;
        WarrantyStatus = warrantyStatus;
        DateTime = null;
        AllottedTo = null;
    }

    public String setDateTime(Date date) {
        return getDateFormat().format(date);
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

    public String getDateTimeString() {
        return DateTime;
    }

    public String getWarrantyStatus() {
        return WarrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        WarrantyStatus = warrantyStatus;
    }

    public String getComplaintType() {
        return ComplaintType;
    }

    public void setComplaintType(String complaintType) {
        ComplaintType = complaintType;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumber(String productNumber) {
        ProductNumber = productNumber;
    }

    public String getProductDetail() {
        return ProductDetail;
    }

    public void setProductDetail(String productDetail) {
        ProductDetail = productDetail;
    }

    public String getNatureOfSite() {
        return NatureOfSite;
    }

    public void setNatureOfSite(String natureOfSite) {
        NatureOfSite = natureOfSite;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getSiteDetails() {
        return SiteDetails;
    }

    public void setSiteDetails(String siteDetails) {
        SiteDetails = siteDetails;
    }

    public int getNumberOfVisits() {
        return NumberOfVisits;
    }

    public String getNumberOfVisitsString() {
        return NumberOfVisits + "";
    }

    public void setNumberOfVisits(int numberOfVisits) {
        NumberOfVisits = numberOfVisits;
    }

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Email", getEmail());
        map.put("CallNumber", getCallNumber());
        map.put("DateTime", getDateTimeString());
        map.put("ConcernName", getConcernName());
        map.put("IsCompleted", getIsCompleted());
        map.put("SiteDetails", getSiteDetails());
        map.put("CustomerName", getCustomerName());
        map.put("NatureOfSite", getNatureOfSite());
        map.put("ConcernPhone", getConcernPhone());
        map.put("ComplaintType", getComplaintType());
        map.put("ProductDetail", getProductDetail());
        map.put("ProductNumber", getProductNumber());
        map.put("WarrantyStatus", getWarrantyStatus());
        map.put("NumberOfVisits", getNumberOfVisitsString());
        return map;
    }

    public Map<String, String> getEditMap() {
        Map<String, String> map = new HashMap<>();
        map.put("CallNumber", getCallNumber());
        map.put("ConcernName", getConcernName());
        map.put("IsCompleted", getIsCompleted());
        map.put("SiteDetails", getSiteDetails());
        map.put("CustomerName", getCustomerName());
        map.put("NatureOfSite", getNatureOfSite());
        map.put("ConcernPhone", getConcernPhone());
        map.put("ComplaintType", getComplaintType());
        map.put("ProductDetail", getProductDetail());
        map.put("ProductNumber", getProductNumber());
        map.put("WarrantyStatus", getWarrantyStatus());
        return map;
    }
}