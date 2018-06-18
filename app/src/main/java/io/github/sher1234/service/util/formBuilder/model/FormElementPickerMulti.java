package io.github.sher1234.service.util.formBuilder.model;

import java.util.ArrayList;
import java.util.List;

public class FormElementPickerMulti extends BaseFormElement {

    private String pickerTitle;
    private List<String> options;
    private List<String> optionsSelected;
    private String positiveText = "Ok";
    private String negativeText = "Cancel";

    public FormElementPickerMulti() {
    }

    public static FormElementPickerMulti createInstance() {
        FormElementPickerMulti FormElementPickerMulti = new FormElementPickerMulti();
        FormElementPickerMulti.setType(BaseFormElement.TYPE_PICKER_MULTI);
        return FormElementPickerMulti;
    }

    public FormElementPickerMulti setTag(int mTag) {
        return (FormElementPickerMulti) super.setTag(mTag);
    }

    public FormElementPickerMulti setType(int mType) {
        return (FormElementPickerMulti) super.setType(mType);
    }

    public FormElementPickerMulti setTitle(String mTitle) {
        return (FormElementPickerMulti) super.setTitle(mTitle);
    }

    public FormElementPickerMulti setValue(String mValue) {
        return (FormElementPickerMulti) super.setValue(mValue);
    }

    public FormElementPickerMulti setHint(String mHint) {
        return (FormElementPickerMulti) super.setHint(mHint);
    }

    public FormElementPickerMulti setRequired(boolean required) {
        return (FormElementPickerMulti) super.setRequired(required);
    }

    public List<String> getOptions() {
        return (this.options == null) ? new ArrayList<String>() : this.options;
    }

    // custom setters
    public FormElementPickerMulti setOptions(List<String> mOptions) {
        this.options = mOptions;
        return this;
    }

    public List<String> getOptionsSelected() {
        return (this.optionsSelected == null) ? new ArrayList<String>() : this.optionsSelected;
    }

    public FormElementPickerMulti setOptionsSelected(List<String> mOptionsSelected) {
        this.optionsSelected = mOptionsSelected;
        return this;
    }

    public String getPickerTitle() {
        return this.pickerTitle;
    }

    public FormElementPickerMulti setPickerTitle(String title) {
        this.pickerTitle = title;
        return this;
    }

    public String getPositiveText() {
        return this.positiveText;
    }

    public FormElementPickerMulti setPositiveText(String positiveText) {
        this.positiveText = positiveText;
        return this;
    }

    public String getNegativeText() {
        return this.negativeText;
    }

    public FormElementPickerMulti setNegativeText(String negativeText) {
        this.negativeText = negativeText;
        return this;
    }

}