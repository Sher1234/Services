package io.github.sher1234.service.util.form.model;

public class FormElementSingleLine extends FormElement {

    private FormElementSingleLine() {
    }

    public static FormElementSingleLine createInstance(boolean edit) {
        FormElementSingleLine formElementSingleLine = new FormElementSingleLine();
        formElementSingleLine.setType(edit ? TEXT_SINGLELINE_EDIT : TEXT_SINGLELINE_VIEW);
        return formElementSingleLine;
    }

    public FormElementSingleLine setTag(int mTag) {
        return (FormElementSingleLine) super.setTag(mTag);
    }

    public FormElementSingleLine setType(int mType) {
        return (FormElementSingleLine) super.setType(mType);
    }

    public FormElementSingleLine setTitle(String mTitle) {
        return (FormElementSingleLine) super.setTitle(mTitle);
    }

    public FormElementSingleLine setValue(String mValue) {
        return (FormElementSingleLine) super.setValue(mValue);
    }

    public FormElementSingleLine setHint(String mHint) {
        return (FormElementSingleLine) super.setHint(mHint);
    }

    public FormElementSingleLine setRequired(boolean required) {
        return (FormElementSingleLine) super.setRequired(required);
    }

}