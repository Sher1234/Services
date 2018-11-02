package io.github.sher1234.service.util.form.model;

import androidx.annotation.NonNull;

public class FormElement {

    public static final int DIVIDER = 0;

    public static final int HEADER_EDIT = 1;
    public static final int HEADER_VIEW = -1;

    public static final int TEXT_SINGLELINE_EDIT = 2;
    public static final int TEXT_SINGLELINE_VIEW = -2;

    public static final int TEXT_MULTILINE_EDIT = 3;
    public static final int TEXT_MULTILINE_VIEW = -3;

    public static final int TEXT_NUMBER_EDIT = 4;
    public static final int TEXT_NUMBER_VIEW = -4;

    public static final int TEXT_EMAIL_EDIT = 5;
    public static final int TEXT_EMAIL_VIEW = -5;

    public static final int TEXT_PHONE_EDIT = 6;
    public static final int TEXT_PHONE_VIEW = -6;

    public static final int PICKER_EDIT = 7;
    public static final int PICKER_VIEW = -7;

    public static final int PICKER_DATE_EDIT = 8;
    public static final int PICKER_DATE_VIEW = -8;

    public static final int RATING_EDIT = 9;
    public static final int RATING_VIEW = -9;

    private int mTag;
    private int mType;
    private String mHint;
    private String mTitle;
    private String mValue;
    private boolean mEnabled = true;
    private boolean mRequired = false;

    public int getTag() {
        return this.mTag;
    }

    FormElement setTag(int mTag) {
        this.mTag = mTag;
        return this;
    }

    public int getType() {
        return this.mType;
    }

    FormElement setType(int mType) {
        this.mType = mType;
        return this;
    }

    public String getTitle() {
        return this.mTitle;
    }

    FormElement setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getValue() {
        return (this.mValue == null) ? "" : this.mValue;
    }

    public FormElement setValue(String mValue) {
        this.mValue = mValue;
        return this;
    }

    public String getHint() {
        return (this.mHint == null) ? "" : this.mHint;
    }

    public FormElement setHint(String mHint) {
        this.mHint = mHint;
        return this;
    }

    public boolean isRequired() {
        return this.mRequired;
    }

    FormElement setRequired(boolean required) {
        this.mRequired = required;
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public FormElement setEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "BaseFormElement{" +
                "mTag=" + mTag +
                ", mType=" + mType +
                ", mTitle='" + mTitle + '\'' +
                ", mValue='" + mValue + '\'' +
                ", mHint='" + mHint + '\'' +
                ", mRequired=" + mRequired +
                '}';
    }
}