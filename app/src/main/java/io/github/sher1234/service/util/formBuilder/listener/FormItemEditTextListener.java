package io.github.sher1234.service.util.formBuilder.listener;

import android.text.Editable;
import android.text.TextWatcher;

import io.github.sher1234.service.util.formBuilder.adapter.FormAdapter;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;

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

        BaseFormElement baseFormElement = formAdapter.getElements().get(position);
        String currentValue = baseFormElement.getValue();
        String newValue = charSequence.toString();

        if (!currentValue.equals(newValue)) {
            baseFormElement.setValue(newValue);
            if (formAdapter.getValueChangeListener() != null)
                formAdapter.getValueChangeListener().onValueChanged(baseFormElement);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}