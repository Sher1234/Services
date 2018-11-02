package io.github.sher1234.service.util.form.viewholder;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.widget.AppCompatTextView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.util.form.listener.ReloadListener;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementDatePicker;

@SuppressWarnings("all")
public class FormElementDatePickerViewHolder extends FormViewHolder {

    private final ReloadListener reloadListener;
    private final AppCompatTextView textView;
    private final TextInputEditText editText;
    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendarCurrentDate;
    private FormElement mFormElement;
    private int mPosition;

    private final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendarCurrentDate.set(Calendar.YEAR, year);
            mCalendarCurrentDate.set(Calendar.MONTH, monthOfYear);
            mCalendarCurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormatDate = ((FormElementDatePicker) mFormElement).getDateFormat();
            SimpleDateFormat sdfDate = new SimpleDateFormat(myFormatDate, Locale.US);

            String currentValue = mFormElement.getValue();
            String newValue = sdfDate.format(mCalendarCurrentDate.getTime());
            ((FormElementDatePicker) mFormElement).setDate(mCalendarCurrentDate.getTime());

            if (!currentValue.equals(newValue)) {
                reloadListener.updateValue(mPosition, newValue);
                ((FormElementDatePicker) mFormElement).setDate(mCalendarCurrentDate.getTime());
            }
        }

    };

    public FormElementDatePickerViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        this.reloadListener = reloadListener;
        editText = v.findViewById(R.id.editText);
        textView = v.findViewById(R.id.textView);
        mCalendarCurrentDate = Calendar.getInstance();
    }

    @Override
    public void bind(int position, FormElement formElement, final Context context) {
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