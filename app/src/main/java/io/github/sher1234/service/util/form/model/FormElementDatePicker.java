package io.github.sher1234.service.util.form.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormElementDatePicker extends FormElement {
    private Date date;
    private String dateFormat;

    private FormElementDatePicker() {
    }

    public static FormElementDatePicker createInstance(boolean edit) {
        return new FormElementDatePicker().setType(edit ? PICKER_DATE_EDIT : PICKER_DATE_VIEW)
                .setDateFormat("dd/MM/yy");
    }

    public FormElementDatePicker setTag(int mTag) {
        return (FormElementDatePicker) super.setTag(mTag);
    }

    public FormElementDatePicker setType(int mType) {
        return (FormElementDatePicker) super.setType(mType);
    }

    public FormElementDatePicker setTitle(String mTitle) {
        return (FormElementDatePicker) super.setTitle(mTitle);
    }

    public FormElementDatePicker setValue(String mValue) {
        return (FormElementDatePicker) super.setValue(mValue);
    }

    public FormElementDatePicker setHint(String mHint) {
        return (FormElementDatePicker) super.setHint(mHint);
    }

    public FormElementDatePicker setRequired(boolean required) {
        return (FormElementDatePicker) super.setRequired(required);
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public FormElementDatePicker setDateFormat(String format) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat, Locale.US);
        this.setValue(dateFormat.format(date));
        this.date = date;
    }
}
