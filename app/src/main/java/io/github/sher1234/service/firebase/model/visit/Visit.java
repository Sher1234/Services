package io.github.sher1234.service.firebase.model.visit;

import android.location.Location;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;

import io.github.sher1234.service.functions.v4.user.FireUser;

import static io.github.sher1234.service.firebase.model.request.Handler.visitId;

public class Visit implements Serializable {

    @PropertyName("observation") public String observation;
    @PropertyName("customer_phone") public String cPhone;
    @PropertyName("customer_email") public String cEmail;
    @PropertyName("customer_name") public String cName;
    @PropertyName("location") public Location location;
    @PropertyName("feedback") public String feedback;
    @PropertyName("action") public String action;
    @PropertyName("rating") public int rating;
    @PropertyName("sign") public String sign;
    @PropertyName("start") public Date start;
    @PropertyName("uid") public String uid;
    @PropertyName("vid") public String vid;
    @PropertyName("rid") public String rid;
    @PropertyName("end") public Date end;

    public Visit() {
    }

    public Visit end(Visit visit, int rating, String sign, String action, String feedback,
                     String customerName, String customerEmail, String customerPhone, String observation) {
        return edit(visit.vid, visit.rid, visit.start, new Date(), rating, visit.uid, sign, action,
                feedback, customerName, customerEmail, customerPhone, visit.location, observation);
    }

    public Visit edit(String vid, String rid, Date start, Date end, int rating, String uid, String sign,
                      String action, String feedback, String customerName, String customerEmail,
                      String customerPhone, Location location, String observation) {
        this.observation = observation;
        this.cEmail = customerEmail;
        this.cPhone = customerPhone;
        this.cName = customerName;
        this.location = location;
        this.feedback = feedback;
        this.action = action;
        this.rating = rating;
        this.start = start;
        this.sign = sign;
        this.vid = vid;
        this.uid = uid;
        this.rid = rid;
        this.end = end;
        return this;
    }

    public Visit start(String rid, Location location) {
        return edit(visitId(), rid, new Date(), null, 0, new FireUser().getUser().uid,
                null, null, null, null, null,
                null, location, null);
    }
}