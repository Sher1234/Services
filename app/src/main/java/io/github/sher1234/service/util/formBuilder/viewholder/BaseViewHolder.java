package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.sher1234.service.util.formBuilder.listener.FormItemEditTextListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;

public class BaseViewHolder extends RecyclerView.ViewHolder implements BaseViewHolderInterface {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public FormItemEditTextListener getListener() {
        return null;
    }

    @Override
    public void bind(int position, BaseFormElement formElement, Context context) {

    }

}