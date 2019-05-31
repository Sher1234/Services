package io.github.sher1234.service.firebase.model.request;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public enum Warranty implements Serializable {
    OutOfWarranty("Out of Warranty", -1),
    InWarranty("Warranty Active", 1),
    ToBeChecked("To be checked", 0);

    @PropertyName("type") public final String type;
    @PropertyName("id") public final int id;

    Warranty(String type, int id) {
        this.type = type;
        this.id = id;
    }
}