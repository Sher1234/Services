package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;

public class FormElementHeader extends BaseViewHolder {

    public AppCompatTextView mTextViewTitle;

    public FormElementHeader(View v) {
        super(v);
        mTextViewTitle = v.findViewById(R.id.formElementTitle);
    }

    @Override
    public void bind(int position, BaseFormElement formElement, final Context context) {
        mTextViewTitle.setText(formElement.getTitle());
    }

}