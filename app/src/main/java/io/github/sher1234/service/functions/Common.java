package io.github.sher1234.service.functions;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import androidx.annotation.NonNull;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.form.FormBuilder;
import io.github.sher1234.service.util.form.model.FormElementDatePicker;
import io.github.sher1234.service.util.form.model.FormElementRating;

public class Common {

    private MaterialDialog progress;

    public boolean isEmailInvalid(@NotNull String s) {
        return s.length() < 5 || !s.contains("@") || !s.contains(".");
    }

    public boolean isPasswordInvalid(@NotNull String s) {
        return s.length() < 8;
    }

    public boolean isEmployeeIdInvalid(@NotNull String s) {
        return s.length() < 10;
    }

    public boolean isNameInvalid(@NotNull String s) {
        return s.length() < 3;
    }

    public boolean isPhoneInvalid(@NotNull String s) {
        return s.length() != 10;
    }

    @NonNull
    public String getString(@NotNull TextInputEditText editText) {
        if (editText.getText() != null)
            return editText.getText().toString();
        return "";
    }

    public void showProgressDialog(@NotNull Context context) {
        progress = MaterialDialog.Progress(context);
        progress.show();
    }

    public void dismissProgressDialog() {
        if (progress != null)
            progress.dismiss();
    }

    String getFormValue(@NotNull FormBuilder builder, int tag) {
        return builder.getFormElement(tag).getValue();
    }

    Date getFormDate(@NotNull FormBuilder builder, int tag) {
        return ((FormElementDatePicker) builder.getFormElement(tag)).getDate();
    }

    void setFormDate(@NotNull FormBuilder builder, int tag, Date date) {
        ((FormElementDatePicker) builder.getFormElement(tag)).setDate(date);
        builder.getFormAdapter().notifyDataSetChanged();
    }

    void onResetForm(@NotNull FormBuilder builder, int tag) {
        for (int i = 1; i < tag; i++)
            builder.getFormElement(i).setValue("");
        builder.getFormAdapter().notifyDataSetChanged();
    }

    void setFormValue(@NotNull FormBuilder builder, int tag, String s) {
        builder.getFormElement(tag).setValue(s);
        builder.getFormAdapter().notifyDataSetChanged();
    }

    void setFormRating(@NotNull FormBuilder builder, int tag, int s) {
        ((FormElementRating) builder.getFormElement(tag)).setRating(s);
        builder.getFormAdapter().notifyDataSetChanged();
    }

    int getFormRating(@NotNull FormBuilder builder, int tag) {
        return (int) ((FormElementRating) builder.getFormElement(tag)).getRating();
    }

}