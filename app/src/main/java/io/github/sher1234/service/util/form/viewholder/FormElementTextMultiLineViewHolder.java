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

public class FormElementTextMultiLineViewHolder extends FormViewHolder {

    private final AppCompatTextView textView;
    private final TextInputEditText editText;
    private final FormItemEditTextListener mFormCustomEditTextListener;

    public FormElementTextMultiLineViewHolder(View v, FormItemEditTextListener listener) {
        super(v);
        textView = v.findViewById(R.id.textView);
        editText = v.findViewById(R.id.editText);
        mFormCustomEditTextListener = listener;
        editText.addTextChangedListener(mFormCustomEditTextListener);
        editText.setMaxLines(8);
        editText.setSingleLine(false);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    @Override
    public FormItemEditTextListener getListener() {
        return mFormCustomEditTextListener;
    }

    @Override
    public void bind(int position, FormElement formElement, final Context context) {
        editText.setHint(formElement.getHint());
        textView.setText(formElement.getTitle());
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