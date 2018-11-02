package io.github.sher1234.service.util.form.model;

public class FormHeader extends FormElement {

    private FormHeader() {
    }

    public static FormHeader createInstance(String title, boolean edit) {
        FormHeader formHeader = new FormHeader();
        formHeader.setType(edit ? HEADER_EDIT : HEADER_VIEW);
        formHeader.setTitle(title);
        return formHeader;
    }
}