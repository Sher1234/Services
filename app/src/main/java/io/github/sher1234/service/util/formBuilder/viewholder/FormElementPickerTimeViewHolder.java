package io.github.sher1234.service.util.formBuilder.viewholder;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
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

@SuppressWarnings("all")
public class FormElementPickerTimeViewHolder extends BaseViewHolder {

    private int mPosition;
    private final TextInputEditText editText;
    private final AppCompatTextView textView;
    private final ReloadListener reloadListener;
    private final TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private BaseFormElement element;
    private final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            String myFormatTime = ((FormElementPickerTime) element).getTimeFormat(); // custom format
            SimpleDateFormat sdfTime = new SimpleDateFormat(myFormatTime, Locale.US);

            String currentValue = element.getValue();
            String newValue = sdfTime.format(calendar.getTime());

            if (!currentValue.equals(newValue)) {
                reloadListener.updateValue(mPosition, newValue);
            }
        }
    };

    public FormElementPickerTimeViewHolder(View v, Context context, ReloadListener reloadListener) {
        super(v);
        calendar = Calendar.getInstance();
        this.reloadListener = reloadListener;
        editText = v.findViewById(R.id.editText);
        textView = v.findViewById(R.id.textView);
        timePickerDialog = new TimePickerDialog(context,
                time,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                false);
    }

    @Override
    public void bind(int position, BaseFormElement formElement, final Context context) {
        element = formElement;
        mPosition = position;

        editText.setHint(formElement.getHint());
        textView.setText(formElement.getTitle());
        editText.setFocusableInTouchMode(false);
        editText.setText(formElement.getValue());
        textView.setEnabled(formElement.isEnabled());
        editText.setEnabled(formElement.isEnabled());

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
    }

}