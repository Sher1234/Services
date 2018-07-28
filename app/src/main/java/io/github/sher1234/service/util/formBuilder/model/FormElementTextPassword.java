package io.github.sher1234.service.util.formBuilder.model;

@SuppressWarnings("all")
public class FormElementTextPassword extends BaseFormElement {

    private FormElementTextPassword() {
    }

    public static FormElementTextPassword createInstance() {
        FormElementTextPassword FormElementTextPassword = new FormElementTextPassword();
        FormElementTextPassword.setType(BaseFormElement.TYPE_EDITTEXT_PASSWORD);
        return FormElementTextPassword;
    }

    public FormElementTextPassword setTag(int mTag) {
        return (FormElementTextPassword) super.setTag(mTag);
    }

    public FormElementTextPassword setType(int mType) {
        return (FormElementTextPassword) super.setType(mType);
    }

    public FormElementTextPassword setTitle(String mTitle) {
        return (FormElementTextPassword) super.setTitle(mTitle);
    }

    public FormElementTextPassword setValue(String mValue) {
        return (FormElementTextPassword) super.setValue(mValue);
    }

    public FormElementTextPassword setHint(String mHint) {
        return (FormElementTextPassword) super.setHint(mHint);
    }

    public FormElementTextPassword setRequired(boolean required) {
        return (FormElementTextPassword) super.setRequired(required);
    }

}