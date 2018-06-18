package io.github.sher1234.service.util.formBuilder.model;

import java.util.ArrayList;
import java.util.List;

public class FormElementPickerSingle extends BaseFormElement {

    private String pickerTitle;
    private List<String> mOptions;
    private List<String> mOptionsSelected;

    public FormElementPickerSingle() {
    }

    public static FormElementPickerSingle createInstance() {
        FormElementPickerSingle formElementPickerSingle = new FormElementPickerSingle();
        formElementPickerSingle.setType(BaseFormElement.TYPE_PICKER_SINGLE);
        formElementPickerSingle.setPickerTitle("Pick one");
        return formElementPickerSingle;
    }

    public FormElementPickerSingle setTag(int mTag) {
        return (FormElementPickerSingle) super.setTag(mTag);
    }

    public FormElementPickerSingle setType(int mType) {
        return (FormElementPickerSingle) super.setType(mType);
    }

    public FormElementPickerSingle setTitle(String mTitle) {
        return (FormElementPickerSingle) super.setTitle(mTitle);
    }

    public FormElementPickerSingle setValue(String mValue) {
        return (FormElementPickerSingle) super.setValue(mValue);
    }

    public FormElementPickerSingle setHint(String mHint) {
        return (FormElementPickerSingle) super.setHint(mHint);
    }

    public FormElementPickerSingle setRequired(boolean required) {
        return (FormElementPickerSingle) super.setRequired(required);
    }

    public List<String> getOptions() {
        return (this.mOptions == null) ? new ArrayList<String>() : this.mOptions;
    }

    public FormElementPickerSingle setOptions(List<String> mOptions) {
        this.mOptions = mOptions;
        return this;
    }

    public List<String> getOptionsSelected() {
        return (this.mOptionsSelected == null) ? new ArrayList<String>() : this.mOptionsSelected;
    }

    public FormElementPickerSingle setOptionsSelected(List<String> mOptionsSelected) {
        this.mOptionsSelected = mOptionsSelected;
        return this;
    }

    public String getPickerTitle() {
        return this.pickerTitle;
    }

    public FormElementPickerSingle setPickerTitle(String title) {
        this.pickerTitle = title;
        return this;
    }

}