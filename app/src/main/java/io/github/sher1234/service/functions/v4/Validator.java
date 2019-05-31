package io.github.sher1234.service.functions.v4;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

public class Validator {

    public Validator() {
    }

    public boolean isIdInvalid(@NotNull TextInputEditText editText) {
        return getString(editText).length() < 10;
    }

    public boolean isPasswordInvalid(@NotNull TextInputEditText editText) {
        return getString(editText).length() < 8;
    }

    public boolean isPhoneInvalid(@NotNull TextInputEditText editText) {
        return getString(editText).length() != 10;
    }

    public boolean isEmailInvalid(@NotNull TextInputEditText editText) {
        return getString(editText).length() < 5 || !getString(editText).contains("@")
                || !getString(editText).contains(".");
    }

    public boolean isNameInvalid(@NotNull TextInputEditText editText) {
        return getString(editText).length() < 3;
    }

    public String getString(@NotNull TextInputEditText editText) {
        return editText.getText() != null?editText.getText().toString():"";
    }
}