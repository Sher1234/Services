package io.github.sher1234.service.util.form.viewholder;

import android.content.Context;

import io.github.sher1234.service.util.form.listener.FormItemEditTextListener;
import io.github.sher1234.service.util.form.model.FormElement;

interface BaseViewHolderInterface {
    FormItemEditTextListener getListener();

    void bind(int position, FormElement formElement, Context context);
}