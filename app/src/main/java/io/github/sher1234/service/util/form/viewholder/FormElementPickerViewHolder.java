package io.github.sher1234.service.util.form.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.util.form.listener.ReloadListener;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementPicker;

public class FormElementPickerViewHolder extends FormViewHolder {

    private int mPosition;
    private final TextInputEditText editText;
    private final AppCompatTextView textView;
    private final ReloadListener reloadListener;
    private FormElementPicker formElementPicker;

    public FormElementPickerViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        this.reloadListener = reloadListener;
        editText = v.findViewById(R.id.editText);
        textView = v.findViewById(R.id.textView);
    }

    @Override
    public void bind(final int position, FormElement formElement, final Context context) {
        mPosition = position;
        formElementPicker = (FormElementPicker) formElement;

        editText.setHint(formElement.getHint());
        editText.setFocusableInTouchMode(false);
        textView.setText(formElement.getTitle());
        editText.setText(formElement.getValue());
        textView.setEnabled(formElement.isEnabled());
        editText.setEnabled(formElement.isEnabled());

        final CharSequence[] options = new CharSequence[formElementPicker.getOptions().size()];
        for (int i = 0; i < formElementPicker.getOptions().size(); i++) {
            options[i] = formElementPicker.getOptions().get(i);
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(formElementPicker.getPickerTitle())
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText(options[which]);
                        formElementPicker.setValue(options[which].toString());
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