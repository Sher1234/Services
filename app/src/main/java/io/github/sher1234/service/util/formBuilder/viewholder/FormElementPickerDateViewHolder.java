package io.github.sher1234.service.util.formBuilder.viewholder;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.formBuilder.listener.ReloadListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerDate;

@SuppressWarnings("all")
public class FormElementPickerDateViewHolder extends BaseViewHolder {

    private final ReloadListener reloadListener;
    private DatePickerDialog mDatePickerDialog;
    private final AppCompatTextView textView;
    private final TextInputEditText editText;
    private Calendar mCalendarCurrentDate;
    private BaseFormElement mFormElement;
    private int mPosition;

    private final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendarCurrentDate.set(Calendar.YEAR, year);
            mCalendarCurrentDate.set(Calendar.MONTH, monthOfYear);
            mCalendarCurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormatDate = ((FormElementPickerDate) mFormElement).getDateFormat();
            SimpleDateFormat sdfDate = new SimpleDateFormat(myFormatDate, Locale.US);

            String currentValue = mFormElement.getValue();
            String newValue = sdfDate.format(mCalendarCurrentDate.getTime());
            ((FormElementPickerDate) mFormElement).setDate(mCalendarCurrentDate.getTime());

            if (!currentValue.equals(newValue)) {
                reloadListener.updateValue(mPosition, newValue);
                ((FormElementPickerDate) mFormElement).setDate(mCalendarCurrentDate.getTime());
            }
        }

    };

    public FormElementPickerDateViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        this.reloadListener = reloadListener;
        editText = v.findViewById(R.id.editText);
        textView = v.findViewById(R.id.textView);
        mCalendarCurrentDate = Calendar.getInstance();
    }

    @Override
    public void bind(int position, BaseFormElement formElement, final Context context) {
        mFormElement = formElement;
        mPosition = position;

        mDatePickerDialog = new DatePickerDialog(context,
                date,
                mCalendarCurrentDate.get(Calendar.YEAR),
                mCalendarCurrentDate.get(Calendar.MONTH),
                mCalendarCurrentDate.get(Calendar.DAY_OF_MONTH));

        editText.setHint(formElement.getHint());
        editText.setFocusableInTouchMode(false);
        textView.setText(formElement.getTitle());
        editText.setText(formElement.getValue());
        textView.setEnabled(formElement.isEnabled());
        editText.setEnabled(formElement.isEnabled());

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });
    }
}