package io.github.sher1234.service.util.form.viewholder;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.AppCompatTextView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.util.form.listener.FormItemEditTextListener;
import io.github.sher1234.service.util.form.model.FormElement;

public class FormElementTextPhoneViewHolder extends FormViewHolder {

    private final AppCompatTextView textView;
    private final TextInputEditText editText;
    private final FormItemEditTextListener textListener;

    public FormElementTextPhoneViewHolder(View v, FormItemEditTextListener listener) {
        super(v);
        textListener = listener;
        textView = v.findViewById(R.id.textView);
        editText = v.findViewById(R.id.editText);
        editText.setRawInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.addTextChangedListener(textListener);
        editText.setMaxLines(1);
    }

    @Override
    public FormItemEditTextListener getListener() {
        return textListener;
    }

    @Override
    public void bind(int position, FormElement formElement, final Context context) {
        textView.setText(formElement.getTitle());
        editText.setText(formElement.getValue());
        editText.setHint(formElement.getHint());
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