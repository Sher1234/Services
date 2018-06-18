package io.github.sher1234.service.util.formBuilder.viewholder;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.listener.ReloadListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerTime;

public class FormElementPickerTimeViewHolder extends BaseViewHolder {

    private AppCompatTextView mTextViewTitle;
    private AppCompatEditText mEditTextValue;
    private TimePickerDialog mTimePickerDialog;
    private Calendar mCalendarCurrentTime;
    private ReloadListener mReloadListener;
    private BaseFormElement mFormElement;
    private int mPosition;
    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendarCurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendarCurrentTime.set(Calendar.MINUTE, minute);

            String myFormatTime = ((FormElementPickerTime) mFormElement).getTimeFormat(); // custom format
            SimpleDateFormat sdfTime = new SimpleDateFormat(myFormatTime, Locale.US);

            String currentValue = mFormElement.getValue();
            String newValue = sdfTime.format(mCalendarCurrentTime.getTime());

            // trigger event only if the value is changed
            if (!currentValue.equals(newValue)) {
                mReloadListener.updateValue(mPosition, newValue);
            }
        }
    };

    public FormElementPickerTimeViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        mTextViewTitle = v.findViewById(R.id.formElementTitle);
        mEditTextValue = v.findViewById(R.id.formElementValue);
        mReloadListener = reloadListener;
        mCalendarCurrentTime = Calendar.getInstance();
        mTimePickerDialog = new TimePickerDialog(context,
                time,
                mCalendarCurrentTime.get(Calendar.HOUR),
                mCalendarCurrentTime.get(Calendar.MINUTE),
                false);
    }

    @Override
    public void bind(int position, BaseFormElement formElement, final Context context) {
        mFormElement = formElement;
        mPosition = position;

        mTextViewTitle.setText(formElement.getTitle());
        mEditTextValue.setText(formElement.getValue());
        mEditTextValue.setHint(formElement.getHint());
        mEditTextValue.setFocusableInTouchMode(false);
        mEditTextValue.setEnabled(formElement.isEnabled());
        mTextViewTitle.setEnabled(formElement.isEnabled());

        mEditTextValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.show();
            }
        });

        mTextViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.show();
            }
        });
    }

}