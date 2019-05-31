package io.github.sher1234.service.firebase.model.request;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.github.sher1234.service.functions.v4.user.FireUser;

public class Handler {

    public static Warranty getWarranty(int id) {
        if (id == -1) return Warranty.OutOfWarranty;
        if (id == 1) return Warranty.InWarranty;
        return Warranty.ToBeChecked;
    }

    public static SiteNature getSiteNature(int id) {
        if (id == -1) return SiteNature.Complaint;
        return SiteNature.NewCommissioning;
    }

    public static Status getStatus(int code) {
        if (code == 1) return Status.Completed;
        if (code == 0) return Status.Pending;
        return Status.Cancelled;
    }

    private static String getRandomId() {
//        String randomStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
//        StringBuilder s = new StringBuilder(8);
//        for (int i = 0; i < 8; i++)
//            s.append(randomStr.charAt((int)(randomStr.length() * Math.random())));
        byte[] array = new byte[8];
        new Random().nextBytes(array);
        String s = new String(array, Charset.forName("UTF-8"));
        return new Date().getTime() + s;
    }

    public static String requestId() {
        return "REQ" + getRandomId();
    }

    public static String visitId() {
        return "VIS" + getRandomId();
    }

    public Request request(String customerName, String customerPhone, String customerDetail,
                           String concernName, String concernPhone, String productId,
                           String productDetail, int warranty, int siteNature, String complaintType,
                           String complaintDetail) {
        List<String> string = new ArrayList<>();
        string.add(new FireUser().getUser().uid);
        return edit(requestId(), new Date(), getStatus(-1), customerName, customerPhone,
                customerDetail, concernName, concernPhone, productId, productDetail, string,
                getWarranty(warranty), getSiteNature(siteNature), complaintType, complaintDetail);
    }

    public Request edit(String rid, Date time, Status status, String customerName,
                        String customerPhone, String customerDetail, String concernName,
                        String concernPhone, String productId, String productDetail,
                        List<String> access, Warranty warranty, SiteNature siteNature,
                        String complaintType, String complaintDetail) {
        Request request = new Request();
        request.complaintDetail = complaintDetail;
        request.customerDetail = customerDetail;
        request.siteNatureCode = siteNature.id;
        request.productDetail = productDetail;
        request.complaintType = complaintType;
        request.customerPhone = customerPhone;
        request.customerName = customerName;
        request.concernPhone = concernPhone;
        request.warrantyCode = warranty.id;
        request.concernName = concernName;
        request.statusCode = status.code;
        request.siteNature = siteNature;
        request.productId = productId;
        request.warranty = warranty;
        request.status = status;
        request.access = access;
        request.time = time;
        request.rid = rid;
        return request;
    }
}