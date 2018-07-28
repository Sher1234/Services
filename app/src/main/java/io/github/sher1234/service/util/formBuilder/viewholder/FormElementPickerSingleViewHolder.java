package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.listener.ReloadListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerSingle;

public class FormElementPickerSingleViewHolder extends BaseViewHolder {

    private int mPosition;
    private final TextInputEditText editText;
    private final AppCompatTextView textView;
    private final ReloadListener reloadListener;
    private BaseFormElement element;
    private FormElementPickerSingle formElementPickerSingle;

    public FormElementPickerSingleViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        this.reloadListener = reloadListener;
        editText = v.findViewById(R.id.editText);
        textView = v.findViewById(R.id.textView);
    }

    @Override
    public void bind(final int position, BaseFormElement formElement, final Context context) {
        element = formElement;
        mPosition = position;
        formElementPickerSingle = (FormElementPickerSingle) element;

        editText.setHint(formElement.getHint());
        editText.setFocusableInTouchMode(false);
        textView.setText(formElement.getTitle());
        editText.setText(formElement.getValue());
        textView.setEnabled(formElement.isEnabled());
        editText.setEnabled(formElement.isEnabled());

        final CharSequence[] options = new CharSequence[formElementPickerSingle.getOptions().size()];
        for (int i = 0; i < formElementPickerSingle.getOptions().size(); i++) {
            options[i] = formElementPickerSingle.getOptions().get(i);
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(formElementPickerSingle.getPickerTitle())
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText(options[which]);
                        formElementPickerSingle.setValue(options[which].toString());
                        reloadListener.updateValue(position, options[which].toString());
                    }
                })
                .create();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

}