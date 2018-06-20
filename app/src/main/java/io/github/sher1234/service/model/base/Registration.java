package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Registration extends Base implements Serializable {

    private String DateTime;
    private int NumberOfVisits;
    private String SiteDetails;
    private String CustomerName;
    private String NatureOfSite;
    private String ProductDetail;
    private String ProductNumber;
    private String ComplaintType;
    private String WarrantyStatus;

    public Registration() {

    }

    public Registration(int numberOfVisits, String siteDetails, String customerName,
                        String natureOfSite, String productDetail, String productNumber,
                        String complaintType, String warrantyStatus, Date dateTime, String email,
                        String callNumber, String concernName, String concernPhone,
                        boolean isCompleted) {
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

    public Map<String, String> getRegistrationMap() {
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

    public Query getQuery() {
        Query query = new Query();
        String s = "INSERT INTO `RegisteredCallsX` (`CallNumber`, `ComplaintType`, `ConcernName`, " +
                "`ConcernPhone`, `CustomerName`, `DateTime`, `IsCompleted`, `NatureOfSite`, " +
                "`NumberOfVisits`, `ProductNumber`, `ProductDetail`, `SiteDetails`, `Email`, " +
                "`WarrantyStatus`) VALUES ('" + getCallNumber() + "', '" + getComplaintType() +
                "', '" + getConcernName() + "', '" + getConcernPhone() + "', '" + getCustomerName() +
                "', '" + getDateTimeString() + "', '" + getIsCompleted() + "', '" + getNatureOfSite() +
                "', '" + getNumberOfVisitsString() + "', '" + getProductNumber() + "', '" +
                getProductDetail() + "', '" + getSiteDetails() + "', '" + getEmail() + "', '" +
                getWarrantyStatus() + "');";
        query.setQuery(s);
        query.setTable("None");
        return query;
    }
}