package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;

import io.github.sher1234.service.util.formBuilder.listener.FormItemEditTextListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;

public interface BaseViewHolderInterface {
    FormItemEditTextListener getListener();

    void bind(int position, BaseFormElement formElement, Context context);
}