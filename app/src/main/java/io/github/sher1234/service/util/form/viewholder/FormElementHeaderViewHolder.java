package io.github.sher1234.service.util.form.viewholder;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.util.form.model.FormElement;

public class FormElementHeaderViewHolder extends FormViewHolder {

    private final AppCompatTextView mTextViewTitle;

    public FormElementHeaderViewHolder(View v) {
        super(v);
        mTextViewTitle = v.findViewById(R.id.formElementTitle);
    }

    @Override
    public void bind(int position, FormElement formElement, final Context context) {
        mTextViewTitle.setText(formElement.getTitle());
    }

}