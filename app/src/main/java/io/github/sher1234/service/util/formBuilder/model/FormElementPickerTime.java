package io.github.sher1234.service.util.formBuilder.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormElementPickerTime extends BaseFormElement {

    private String timeFormat;

    public FormElementPickerTime() {
    }

    public static FormElementPickerTime createInstance() {
        FormElementPickerTime formElementPickerTime = new FormElementPickerTime();
        formElementPickerTime.setType(BaseFormElement.TYPE_PICKER_TIME);
        formElementPickerTime.setTimeFormat("KK:mm a");
        return formElementPickerTime;
    }

    public FormElementPickerTime setTag(int mTag) {
        return (FormElementPickerTime) super.setTag(mTag);
    }

    public FormElementPickerTime setType(int mType) {
        return (FormElementPickerTime) super.setType(mType);
    }

    public FormElementPickerTime setTitle(String mTitle) {
        return (FormElementPickerTime) super.setTitle(mTitle);
    }

    public FormElementPickerTime setValue(String mValue) {
        return (FormElementPickerTime) super.setValue(mValue);
    }

    public FormElementPickerTime setHint(String mHint) {
        return (FormElementPickerTime) super.setHint(mHint);
    }

    public FormElementPickerTime setRequired(boolean required) {
        return (FormElementPickerTime) super.setRequired(required);
    }

    public String getTimeFormat() {
        return this.timeFormat;
    }

    public FormElementPickerTime setTimeFormat(String format) {
        checkValidTimeFormat(format);
        this.timeFormat = format;
        return this;
    }

    private void checkValidTimeFormat(String format) {
        try {
            new SimpleDateFormat(format, Locale.US);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Time format is not correct: " + e.getMessage());
        }
    }

}