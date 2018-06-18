package io.github.sher1234.service.util.formBuilder.model;

public class FormElementSwitch extends BaseFormElement {

    private boolean checked;
    private String positiveText;
    private String negativeText;

    public FormElementSwitch() {
    }

    public static FormElementSwitch createInstance() {
        FormElementSwitch FormElementSwitch = new FormElementSwitch();
        FormElementSwitch.setType(BaseFormElement.TYPE_SWITCH);
        return FormElementSwitch;
    }

    public FormElementSwitch setTag(int mTag) {
        return (FormElementSwitch) super.setTag(mTag);
    }

    public FormElementSwitch setType(int mType) {
        return (FormElementSwitch) super.setType(mType);
    }

    public FormElementSwitch setTitle(String mTitle) {
        return (FormElementSwitch) super.setTitle(mTitle);
    }

    public FormElementSwitch setValue(String mValue) {
        return (FormElementSwitch) super.setValue(mValue);
    }

    public FormElementSwitch setHint(String mHint) {
        return (FormElementSwitch) super.setHint(mHint);
    }

    public FormElementSwitch setRequired(boolean required) {
        return (FormElementSwitch) super.setRequired(required);
    }

    public FormElementSwitch setSwitchTexts(String positiveText, String negativeText) {
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        return this;
    }

    public String getPositiveText() {
        return this.positiveText;
    }

    public String getNegativeText() {
        return this.negativeText;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}