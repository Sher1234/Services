package io.github.sher1234.service.util.form.model;

public class FormDivider extends FormElement {

    private FormDivider() {
    }

    public static FormDivider createInstance() {
        FormDivider formDivider = new FormDivider();
        formDivider.setType(DIVIDER);
        return formDivider;
    }
}