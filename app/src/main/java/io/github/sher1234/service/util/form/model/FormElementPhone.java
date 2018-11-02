package io.github.sher1234.service.util.form.model;

public class FormElementPhone extends FormElement {

    private FormElementPhone() {
    }

    public static FormElementPhone createInstance(boolean edit) {
        FormElementPhone formElementPhone = new FormElementPhone();
        formElementPhone.setType(edit ? TEXT_PHONE_EDIT : TEXT_PHONE_VIEW);
        return formElementPhone;
    }

    public FormElementPhone setTag(int mTag) {
        return (FormElementPhone) super.setTag(mTag);
    }

    public FormElementPhone setType(int mType) {
        return (FormElementPhone) super.setType(mType);
    }

    public FormElementPhone setTitle(String mTitle) {
        return (FormElementPhone) super.setTitle(mTitle);
    }

    public FormElementPhone setValue(String mValue) {
        return (FormElementPhone) super.setValue(mValue);
    }

    public FormElementPhone setHint(String mHint) {
        return (FormElementPhone) super.setHint(mHint);
    }

    public FormElementPhone setRequired(boolean required) {
        return (FormElementPhone) super.setRequired(required);
    }

}