package io.github.sher1234.service.util.form.model;

public class FormElementMultiLine extends FormElement {

    private FormElementMultiLine() {
    }

    public static FormElementMultiLine createInstance(boolean edit) {
        FormElementMultiLine FormElementMultiLine = new FormElementMultiLine();
        FormElementMultiLine.setType(edit ? TEXT_MULTILINE_EDIT : TEXT_MULTILINE_VIEW);
        return FormElementMultiLine;
    }

    public FormElementMultiLine setTag(int mTag) {
        return (FormElementMultiLine) super.setTag(mTag);
    }

    public FormElementMultiLine setType(int mType) {
        return (FormElementMultiLine) super.setType(mType);
    }

    public FormElementMultiLine setTitle(String mTitle) {
        return (FormElementMultiLine) super.setTitle(mTitle);
    }

    public FormElementMultiLine setValue(String mValue) {
        return (FormElementMultiLine) super.setValue(mValue);
    }

    public FormElementMultiLine setHint(String mHint) {
        return (FormElementMultiLine) super.setHint(mHint);
    }

    public FormElementMultiLine setRequired(boolean required) {
        return (FormElementMultiLine) super.setRequired(required);
    }

}