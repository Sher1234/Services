package io.github.sher1234.service.util.form.model;

@SuppressWarnings("all")
public class FormElementNumber extends FormElement {

    private FormElementNumber() {
    }

    public static FormElementNumber createInstance(boolean edit) {
        FormElementNumber FormElementNumber = new FormElementNumber();
        FormElementNumber.setType(edit ? TEXT_NUMBER_EDIT : TEXT_NUMBER_VIEW);
        return FormElementNumber;
    }

    public FormElementNumber setTag(int mTag) {
        return (FormElementNumber) super.setTag(mTag);
    }

    public FormElementNumber setType(int mType) {
        return (FormElementNumber) super.setType(mType);
    }

    public FormElementNumber setTitle(String mTitle) {
        return (FormElementNumber) super.setTitle(mTitle);
    }

    public FormElementNumber setValue(String mValue) {
        return (FormElementNumber) super.setValue(mValue);
    }

    public FormElementNumber setHint(String mHint) {
        return (FormElementNumber) super.setHint(mHint);
    }

    public FormElementNumber setRequired(boolean required) {
        return (FormElementNumber) super.setRequired(required);
    }

}