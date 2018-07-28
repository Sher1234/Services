package io.github.sher1234.service.util.formBuilder.model;

@SuppressWarnings("all")
public class FormElementTextNumber extends BaseFormElement {

    private FormElementTextNumber() {
    }

    public static FormElementTextNumber createInstance() {
        FormElementTextNumber FormElementTextNumber = new FormElementTextNumber();
        FormElementTextNumber.setType(BaseFormElement.TYPE_EDITTEXT_NUMBER);
        return FormElementTextNumber;
    }

    public FormElementTextNumber setTag(int mTag) {
        return (FormElementTextNumber) super.setTag(mTag);
    }

    public FormElementTextNumber setType(int mType) {
        return (FormElementTextNumber) super.setType(mType);
    }

    public FormElementTextNumber setTitle(String mTitle) {
        return (FormElementTextNumber) super.setTitle(mTitle);
    }

    public FormElementTextNumber setValue(String mValue) {
        return (FormElementTextNumber) super.setValue(mValue);
    }

    public FormElementTextNumber setHint(String mHint) {
        return (FormElementTextNumber) super.setHint(mHint);
    }

    public FormElementTextNumber setRequired(boolean required) {
        return (FormElementTextNumber) super.setRequired(required);
    }

}