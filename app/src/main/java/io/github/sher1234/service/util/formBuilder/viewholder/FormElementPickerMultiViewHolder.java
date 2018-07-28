package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.listener.ReloadListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerMulti;

public class FormElementPickerMultiViewHolder extends BaseViewHolder {

    private int mPosition;
    private final TextInputEditText editText;
    private final AppCompatTextView textView;
    private final ReloadListener reloadListener;
    private BaseFormElement element;
    private FormElementPickerMulti formElementPickerMulti;

    public FormElementPickerMultiViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        this.reloadListener = reloadListener;
        editText = v.findViewById(R.id.editText);
        textView = v.findViewById(R.id.textView);
    }

    @Override
    public void bind(final int position, BaseFormElement formElement, final Context context) {
        element = formElement;
        mPosition = position;
        formElementPickerMulti = (FormElementPickerMulti) element;

        editText.setHint(formElement.getHint());
        textView.setText(formElement.getTitle());
        editText.setFocusableInTouchMode(false);
        editText.setText(formElement.getValue());
        textView.setEnabled(formElement.isEnabled());
        editText.setEnabled(formElement.isEnabled());

        // reformat the options in format needed
        final CharSequence[] options = new CharSequence[formElementPickerMulti.getOptions().size()];
        final boolean[] optionsSelected = new boolean[formElementPickerMulti.getOptions().size()];
        final List<Integer> mSelectedItems = new ArrayList<>();

        for (int i = 0; i < formElementPickerMulti.getOptions().size(); i++) {
            options[i] = formElementPickerMulti.getOptions().get(i);
            optionsSelected[i] = false;
            if (formElementPickerMulti.getOptionsSelected().contains(options[i].toString())) {
                optionsSelected[i] = true;
                mSelectedItems.add(i);
            }
        }

        // prepare the dialog
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(formElementPickerMulti.getPickerTitle())
                .setMultiChoiceItems(options, optionsSelected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(formElementPickerMulti.getPositiveText(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder s = new StringBuilder();
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            s.append(options[mSelectedItems.get(i)]);

                            if (i < mSelectedItems.size() - 1) {
                                s.append(", ");
                            }
                        }
                        editText.setText(s.toString());
                        reloadListener.updateValue(position, s.toString());
                    }
                })
                .setNegativeButton(formElementPickerMulti.getNegativeText(), null)
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