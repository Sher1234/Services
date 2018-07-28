package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.listener.FormItemEditTextListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;

public class FormElementTextEmailViewHolder extends BaseViewHolder {

    private final AppCompatTextView textView;
    private final TextInputEditText editText;
    private final FormItemEditTextListener textListener;

    public FormElementTextEmailViewHolder(View v, FormItemEditTextListener listener) {
        super(v);
        textListener = listener;
        editText = v.findViewById(R.id.editText);
        textView = v.findViewById(R.id.textView);
        editText.addTextChangedListener(textListener);
        editText.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    @Override
    public FormItemEditTextListener getListener() {
        return textListener;
    }

    @Override
    public void bind(int position, BaseFormElement formElement, final Context context) {
        textView.setText(formElement.getTitle());
        editText.setHint(formElement.getHint());
        editText.setText(formElement.getValue());
        textView.setFocusableInTouchMode(formElement.isEnabled());
        editText.setFocusableInTouchMode(formElement.isEnabled());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

}