package io.github.sher1234.service.util.formBuilder.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("all")
public class FormElementPickerDate extends BaseFormElement {

    private Date date;
    private String dateFormat;

    private FormElementPickerDate() {
    }

    public static FormElementPickerDate createInstance() {
        return new FormElementPickerDate().setType(BaseFormElement.TYPE_PICKER_DATE)
                .setDateFormat("dd/MM/yy");
    }

    public FormElementPickerDate setTag(int mTag) {
        return (FormElementPickerDate) super.setTag(mTag);
    }

    public FormElementPickerDate setType(int mType) {
        return (FormElementPickerDate) super.setType(mType);
    }

    public FormElementPickerDate setTitle(String mTitle) {
        return (FormElementPickerDate) super.setTitle(mTitle);
    }

    public FormElementPickerDate setValue(String mValue) {
        return (FormElementPickerDate) super.setValue(mValue);
    }

    public FormElementPickerDate setHint(String mHint) {
        return (FormElementPickerDate) super.setHint(mHint);
    }

    public FormElementPickerDate setRequired(boolean required) {
        return (FormElementPickerDate) super.setRequired(required);
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public FormElementPickerDate setDateFormat(String format) {
        checkValidDateFormat(format);
        this.dateFormat = format;
        return this;
    }

    private void checkValidDateFormat(String format) {
        try {
            new SimpleDateFormat(format, Locale.US);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Date format is not correct: " + e.getMessage());
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}