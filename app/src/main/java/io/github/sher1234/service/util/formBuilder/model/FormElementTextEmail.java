package io.github.sher1234.service.util.formBuilder.model;

public class FormElementTextEmail extends BaseFormElement {

    private FormElementTextEmail() {
    }

    public static FormElementTextEmail createInstance() {
        FormElementTextEmail FormElementTextEmail = new FormElementTextEmail();
        FormElementTextEmail.setType(BaseFormElement.TYPE_EDITTEXT_EMAIL);
        return FormElementTextEmail;
    }

    public FormElementTextEmail setTag(int mTag) {
        return (FormElementTextEmail) super.setTag(mTag);
    }

    public FormElementTextEmail setType(int mType) {
        return (FormElementTextEmail) super.setType(mType);
    }

    public FormElementTextEmail setTitle(String mTitle) {
        return (FormElementTextEmail) super.setTitle(mTitle);
    }

    public FormElementTextEmail setValue(String mValue) {
        return (FormElementTextEmail) super.setValue(mValue);
    }

    public FormElementTextEmail setHint(String mHint) {
        return (FormElementTextEmail) super.setHint(mHint);
    }

    public FormElementTextEmail setRequired(boolean required) {
        return (FormElementTextEmail) super.setRequired(required);
    }

}