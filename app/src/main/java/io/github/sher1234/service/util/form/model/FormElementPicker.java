package io.github.sher1234.service.util.form.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class FormElementPicker extends FormElement {

    private String pickerTitle;
    private List<String> mOptions;
    private List<String> mOptionsSelected;

    private FormElementPicker() {
    }

    public static FormElementPicker createInstance(boolean edit) {
        return new FormElementPicker().setType(edit ? PICKER_EDIT : PICKER_VIEW).setPickerTitle();
    }

    public FormElementPicker setTag(int mTag) {
        return (FormElementPicker) super.setTag(mTag);
    }

    public FormElementPicker setType(int mType) {
        return (FormElementPicker) super.setType(mType);
    }

    public FormElementPicker setTitle(String mTitle) {
        return (FormElementPicker) super.setTitle(mTitle);
    }

    public FormElementPicker setValue(String mValue) {
        return (FormElementPicker) super.setValue(mValue);
    }

    public FormElementPicker setHint(String mHint) {
        return (FormElementPicker) super.setHint(mHint);
    }

    public FormElementPicker setRequired(boolean required) {
        return (FormElementPicker) super.setRequired(required);
    }

    public List<String> getOptions() {
        return (this.mOptions == null) ? new ArrayList<String>() : this.mOptions;
    }

    public FormElementPicker setOptions(List<String> mOptions) {
        this.mOptions = mOptions;
        return this;
    }

    public List<String> getOptionsSelected() {
        return (this.mOptionsSelected == null) ? new ArrayList<String>() : this.mOptionsSelected;
    }

    public FormElementPicker setOptionsSelected(List<String> mOptionsSelected) {
        this.mOptionsSelected = mOptionsSelected;
        return this;
    }

    public String getPickerTitle() {
        return this.pickerTitle;
    }

    private FormElementPicker setPickerTitle() {
        this.pickerTitle = "Pick one";
        return this;
    }

}