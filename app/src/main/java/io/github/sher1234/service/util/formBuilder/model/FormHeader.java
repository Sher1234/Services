package io.github.sher1234.service.util.formBuilder.model;

public class FormHeader extends BaseFormElement {

    public FormHeader() {
    }

    public static FormHeader createInstance(String title) {
        FormHeader formHeader = new FormHeader();
        formHeader.setType(BaseFormElement.TYPE_HEADER);
        formHeader.setTitle(title);
        return formHeader;
    }
}