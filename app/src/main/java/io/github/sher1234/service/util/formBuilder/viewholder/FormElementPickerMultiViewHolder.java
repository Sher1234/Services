package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.listener.ReloadListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerMulti;

public class FormElementPickerMultiViewHolder extends BaseViewHolder {

    private AppCompatTextView mTextViewTitle;
    private AppCompatEditText mEditTextValue;
    private ReloadListener mReloadListener;
    private BaseFormElement mFormElement;
    private FormElementPickerMulti mFormElementPickerMulti;
    private int mPosition;

    public FormElementPickerMultiViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        mTextViewTitle = v.findViewById(R.id.formElementTitle);
        mEditTextValue = v.findViewById(R.id.formElementValue);
        mReloadListener = reloadListener;
    }

    @Override
    public void bind(final int position, BaseFormElement formElement, final Context context) {
        mFormElement = formElement;
        mPosition = position;
        mFormElementPickerMulti = (FormElementPickerMulti) mFormElement;

        mTextViewTitle.setText(formElement.getTitle());
        mEditTextValue.setText(formElement.getValue());
        mEditTextValue.setHint(formElement.getHint());
        mEditTextValue.setFocusableInTouchMode(false);
        mEditTextValue.setEnabled(formElement.isEnabled());
        mTextViewTitle.setEnabled(formElement.isEnabled());

        // reformat the options in format needed
        final CharSequence[] options = new CharSequence[mFormElementPickerMulti.getOptions().size()];
        final boolean[] optionsSelected = new boolean[mFormElementPickerMulti.getOptions().size()];
        final List<Integer> mSelectedItems = new ArrayList<>();

        for (int i = 0; i < mFormElementPickerMulti.getOptions().size(); i++) {
            options[i] = mFormElementPickerMulti.getOptions().get(i);
            optionsSelected[i] = false;
            if (mFormElementPickerMulti.getOptionsSelected().contains(options[i].toString())) {
                optionsSelected[i] = true;
                mSelectedItems.add(i);
            }
        }

        // prepare the dialog
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(mFormElementPickerMulti.getPickerTitle())
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
                .setPositiveButton(mFormElementPickerMulti.getPositiveText(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder s = new StringBuilder();
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            s.append(options[mSelectedItems.get(i)]);

                            if (i < mSelectedItems.size() - 1) {
                                s.append(", ");
                            }
                        }
                        mEditTextValue.setText(s.toString());
                        mReloadListener.updateValue(position, s.toString());
                    }
                })
                .setNegativeButton(mFormElementPickerMulti.getNegativeText(), null)
                .create();

        mEditTextValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        mTextViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

}