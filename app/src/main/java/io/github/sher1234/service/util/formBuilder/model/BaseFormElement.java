package io.github.sher1234.service.util.formBuilder.model;

public class BaseFormElement {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_EDITTEXT_TEXT_SINGLELINE = 1;
    public static final int TYPE_EDITTEXT_TEXT_MULTILINE = 2;
    public static final int TYPE_EDITTEXT_NUMBER = 3;
    public static final int TYPE_EDITTEXT_EMAIL = 4;
    public static final int TYPE_EDITTEXT_PHONE = 5;
    public static final int TYPE_EDITTEXT_PASSWORD = 6;
    public static final int TYPE_PICKER_DATE = 7;
    public static final int TYPE_PICKER_TIME = 8;
    public static final int TYPE_PICKER_SINGLE = 9;
    public static final int TYPE_PICKER_MULTI = 10;
    public static final int TYPE_SWITCH = 11;

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

    BaseFormElement setTag(int mTag) {
        this.mTag = mTag;
        return this;
    }

    public int getType() {
        return this.mType;
    }

    BaseFormElement setType(int mType) {
        this.mType = mType;
        return this;
    }

    public String getTitle() {
        return this.mTitle;
    }

    BaseFormElement setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getValue() {
        return (this.mValue == null) ? "" : this.mValue;
    }

    public BaseFormElement setValue(String mValue) {
        this.mValue = mValue;
        return this;
    }

    public String getHint() {
        return (this.mHint == null) ? "" : this.mHint;
    }

    public BaseFormElement setHint(String mHint) {
        this.mHint = mHint;
        return this;
    }

    public boolean isRequired() {
        return this.mRequired;
    }

    BaseFormElement setRequired(boolean required) {
        this.mRequired = required;
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public BaseFormElement setEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return this;
    }

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