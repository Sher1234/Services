package io.github.sher1234.service.util.form.model;

public class FormElementEmail extends FormElement {

    private FormElementEmail() {
    }

    public static FormElementEmail createInstance(boolean edit) {
        FormElementEmail formElementEmail = new FormElementEmail();
        formElementEmail.setType(edit ? TEXT_EMAIL_EDIT : TEXT_EMAIL_VIEW);
        return formElementEmail;
    }

    public FormElementEmail setTag(int mTag) {
        return (FormElementEmail) super.setTag(mTag);
    }

    public FormElementEmail setType(int mType) {
        return (FormElementEmail) super.setType(mType);
    }

    public FormElementEmail setTitle(String mTitle) {
        return (FormElementEmail) super.setTitle(mTitle);
    }

    public FormElementEmail setValue(String mValue) {
        return (FormElementEmail) super.setValue(mValue);
    }

    public FormElementEmail setHint(String mHint) {
        return (FormElementEmail) super.setHint(mHint);
    }

    public FormElementEmail setRequired(boolean required) {
        return (FormElementEmail) super.setRequired(required);
    }

}