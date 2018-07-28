package io.github.sher1234.service.util.formBuilder.model;

public class FormElementTextPhone extends BaseFormElement {

    private FormElementTextPhone() {
    }

    public static FormElementTextPhone createInstance() {
        FormElementTextPhone FormElementTextPhone = new FormElementTextPhone();
        FormElementTextPhone.setType(BaseFormElement.TYPE_EDITTEXT_PHONE);
        return FormElementTextPhone;
    }

    public FormElementTextPhone setTag(int mTag) {
        return (FormElementTextPhone) super.setTag(mTag);
    }

    public FormElementTextPhone setType(int mType) {
        return (FormElementTextPhone) super.setType(mType);
    }

    public FormElementTextPhone setTitle(String mTitle) {
        return (FormElementTextPhone) super.setTitle(mTitle);
    }

    public FormElementTextPhone setValue(String mValue) {
        return (FormElementTextPhone) super.setValue(mValue);
    }

    public FormElementTextPhone setHint(String mHint) {
        return (FormElementTextPhone) super.setHint(mHint);
    }

    public FormElementTextPhone setRequired(boolean required) {
        return (FormElementTextPhone) super.setRequired(required);
    }

}