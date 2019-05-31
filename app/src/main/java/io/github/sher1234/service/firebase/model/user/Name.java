package io.github.sher1234.service.firebase.model.user;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Name implements Serializable {
    @PropertyName("first") public String first;
    @PropertyName("last") public String last;

    public Name(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public Name() {}

    @NonNull
    @Override
    public String toString() {
        return first + " " + last;
    }
}