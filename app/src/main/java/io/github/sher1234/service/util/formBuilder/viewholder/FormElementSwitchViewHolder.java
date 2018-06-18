package io.github.sher1234.service.util.formBuilder.viewholder;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.listener.ReloadListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementSwitch;

public class FormElementSwitchViewHolder extends BaseViewHolder {

    public AppCompatTextView mTextViewTitle, mTextViewPositive, mTextViewNegative;
    public Switch mSwitch;
    private ReloadListener mReloadListener;
    private BaseFormElement mFormElement;
    private FormElementSwitch mFormElementSwitch;
    private int mPosition;

    public FormElementSwitchViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        mTextViewTitle = v.findViewById(R.id.formElementTitle);
        mTextViewPositive = v.findViewById(R.id.formElementPositiveText);
        mTextViewNegative = v.findViewById(R.id.formElementNegativeText);
        mSwitch = v.findViewById(R.id.formElementSwitch);
        mReloadListener = reloadListener;
    }

    @Override
    public void bind(int position, BaseFormElement formElement, final Context context) {
        mFormElement = formElement;
        mPosition = position;
        mFormElementSwitch = (FormElementSwitch) mFormElement;
        mSwitch.setEnabled(formElement.isEnabled());
        mSwitch.setChecked(mFormElementSwitch.isChecked());
        mTextViewTitle.setEnabled(formElement.isEnabled());

        mTextViewTitle.setText(mFormElementSwitch.getTitle());
        mTextViewPositive.setText(mFormElementSwitch.getPositiveText());
        mTextViewNegative.setText(mFormElementSwitch.getNegativeText());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mReloadListener.updateValue(mPosition, b ? mFormElementSwitch.getPositiveText() : mFormElementSwitch.getNegativeText());
                mFormElementSwitch.setChecked(b);
            }
        });
    }

}