package io.github.sher1234.service.firebase.model.user;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Phone implements Serializable {
    @PropertyName("number") public String number;
    @PropertyName("code") public String code;

    public Phone(String code, String number) {
        this.number = number;
        this.code = code;
    }

    public Phone() {}

    @NonNull
    @Override
    public String toString() {
        return code + "-" + number;
    }
}