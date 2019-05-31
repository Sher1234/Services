package io.github.sher1234.service.firebase.model.request;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.sher1234.service.functions.v4.user.FireUser;

import static io.github.sher1234.service.firebase.model.request.Handler.getSiteNature;
import static io.github.sher1234.service.firebase.model.request.Handler.getStatus;
import static io.github.sher1234.service.firebase.model.request.Handler.getWarranty;
import static io.github.sher1234.service.firebase.model.request.Handler.requestId;


//@SuppressWarnings("all")
public class FireRequest implements Serializable {

    @PropertyName("complaint_detail") public String complaintDetail;
    @PropertyName("customer_detail") public String customerDetail;
    @PropertyName("complaint_type") public String complaintType;
    @PropertyName("customer_phone") public String customerPhone;
    @PropertyName("product_detail") public String productDetail;
    @PropertyName("customer_name") public String customerName;
    @PropertyName("concern_phone") public String concernPhone;
    @PropertyName("concern_name") public String concernName;
    @PropertyName("product_id") public String productId;
    @PropertyName("access") public List<String> access;
    @PropertyName("time") public Date time;
    @PropertyName("rid") public String rid;

    @PropertyName("nature_code")
    public int siteNatureCode;
    @PropertyName("nature") public SiteNature siteNature;

    @PropertyName("warranty_code")
    public int warrantyCode;
    @PropertyName("warranty") public Warranty warranty;

    @PropertyName("status_code")
    public int statusCode;
    @PropertyName("status") public Status status;

    public FireRequest request(String customerName, String customerPhone, String customerDetail,
                               String concernName, String concernPhone, String productId,
                               String productDetail, int warranty, int siteNature, String complaintType,
                               String complaintDetail) {
        List<String> string = new ArrayList<>();
        string.add(new FireUser().getUser().uid);
        return edit(requestId(), new Date(), getStatus(-1), customerName, customerPhone,
                customerDetail, concernName, concernPhone, productId, productDetail, string,
                getWarranty(warranty), getSiteNature(siteNature), complaintType, complaintDetail);
    }

    public FireRequest edit(String rid, Date time, Status status, String customerName,
                            String customerPhone, String customerDetail, String concernName,
                            String concernPhone, String productId, String productDetail,
                            List<String> access, Warranty warranty, SiteNature siteNature,
                            String complaintType, String complaintDetail) {
        this.complaintDetail = complaintDetail;
        this.customerDetail = customerDetail;
        this.siteNatureCode = siteNature.id;
        this.productDetail = productDetail;
        this.complaintType = complaintType;
        this.customerPhone = customerPhone;
        this.customerName = customerName;
        this.concernPhone = concernPhone;
        this.warrantyCode = warranty.id;
        this.concernName = concernName;
        this.statusCode = status.code;
        this.siteNature = siteNature;
        this.productId = productId;
        this.warranty = warranty;
        this.status = status;
        this.access = access;
        this.time = time;
        this.rid = rid;
        return this;
    }

    public FireRequest() {
    }
}