package io.github.sher1234.service.util.form.viewholder;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.util.form.listener.FormItemEditTextListener;
import io.github.sher1234.service.util.form.model.FormElement;

public class FormViewHolder extends RecyclerView.ViewHolder implements BaseViewHolderInterface {

    FormViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public FormItemEditTextListener getListener() {
        return null;
    }

    @Override
    public void bind(int position, FormElement formElement, Context context) {

    }

}