package io.github.sher1234.service.firebase.model.request;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public enum Status implements Serializable {
    Completed("Completed", 1),
    Cancelled("Cancelled", 0),
    Pending("Pending", -1);

    @PropertyName("type") public final String type;
    @PropertyName("code") public final int code;

    Status(String type, int code) {
        this.type = type;
        this.code = code;
    }
}