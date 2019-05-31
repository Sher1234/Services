package io.github.sher1234.service.firebase.model.request;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public enum SiteNature implements Serializable {
    NewCommissioning("New Commissioning", 1),
    Complaint("Complaint", -1);

    @PropertyName("type") public final String type;
    @PropertyName("id") public final int id;

    SiteNature(String type, int id) {
        this.type = type;
        this.id = id;
    }
}