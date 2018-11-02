package io.github.sher1234.service.util.form.listener;

import android.text.Editable;
import android.text.TextWatcher;

import io.github.sher1234.service.util.form.adapter.FormAdapter;
import io.github.sher1234.service.util.form.model.FormElement;

public class FormItemEditTextListener implements TextWatcher {

    private int position;
    private final FormAdapter formAdapter;

    public FormItemEditTextListener(FormAdapter formAdapter) {
        this.formAdapter = formAdapter;
    }

    public void updatePosition(int position) {
        this.position = position;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        FormElement formElement = formAdapter.getElements().get(position);
        String currentValue = formElement.getValue();
        String newValue = charSequence.toString();

        if (!currentValue.equals(newValue)) {
            formElement.setValue(newValue);
            if (formAdapter.getValueChangeListener() != null)
                formAdapter.getValueChangeListener().onValueChanged(formElement);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}