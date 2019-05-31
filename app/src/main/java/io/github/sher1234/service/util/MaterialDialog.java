package io.github.sher1234.service.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import io.github.sher1234.service.R;

@SuppressWarnings("all")
public class MaterialDialog extends Dialog {

    // Common
    private TextView textViewTitle;

    // Dialog Layout
    private Button buttonNeutral;
    private Button buttonNegative;
    private Button buttonPositive;
    private TextView textViewDescription;

    // Progress Layout
    private ProgressBar progressBar;

    public MaterialDialog(@NonNull Context context) {
        super(context, R.style.Application_Dialog);
    }

    public static MaterialDialog Progress(Context context) {
        MaterialDialog materialDialog = new MaterialDialog(context);
        materialDialog.setContentView(R.layout.dialog_progress);
        materialDialog.setProgressView();
        materialDialog.setTitle(R.string.loading);
        materialDialog.setCancelable(false);
        return materialDialog.setProgressIndeterminate(true);
    }

    public static MaterialDialog Dialog(Context context) {
        MaterialDialog materialDialog = new MaterialDialog(context);
        materialDialog.setContentView(R.layout.dialog_layout);
        materialDialog.setDialogView();
        return materialDialog;
    }

    private void setProgressView() {
        textViewTitle = findViewById(R.id.textViewTitle);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setDialogView() {
        textViewTitle = findViewById(R.id.textViewTitle);
        buttonNeutral = findViewById(R.id.buttonNeutral);
        buttonPositive = findViewById(R.id.buttonPositive);
        buttonNegative = findViewById(R.id.buttonNegative);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonPositive.setVisibility(View.GONE);
        buttonNegative.setVisibility(View.GONE);
        buttonNeutral.setVisibility(View.GONE);
    }

    public MaterialDialog setProgress(int i) {
        progressBar.setProgress(i);
        return this;
    }

    private MaterialDialog setProgressIndeterminate(boolean b) {
        progressBar.setIndeterminate(b);
        return this;
    }

    public MaterialDialog positiveButton(String s, final ButtonClick buttonClick) {
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick.onClick(MaterialDialog.this, v);
            }
        });
        buttonPositive.setVisibility(View.VISIBLE);
        buttonPositive.setText(s);
        return this;
    }

    public MaterialDialog positiveButton(@StringRes int s, final ButtonClick buttonClick) {
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick.onClick(MaterialDialog.this, v);
                dismiss();
            }
        });
        buttonPositive.setVisibility(View.VISIBLE);
        buttonPositive.setText(s);
        return this;
    }

    public MaterialDialog negativeButton(String s, final ButtonClick buttonClick) {
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick.onClick(MaterialDialog.this, v);
                dismiss();
            }
        });
        buttonNegative.setVisibility(View.VISIBLE);
        buttonNegative.setText(s);
        return this;
    }

    public MaterialDialog negativeButton(@StringRes int s, final ButtonClick buttonClick) {
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick.onClick(MaterialDialog.this, v);
                dismiss();
            }
        });
        buttonNegative.setVisibility(View.VISIBLE);
        buttonNegative.setText(s);
        return this;
    }

    public MaterialDialog neutralButton(String s, final ButtonClick buttonClick) {
        buttonNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick.onClick(MaterialDialog.this, v);
            }
        });
        buttonNeutral.setVisibility(View.VISIBLE);
        buttonNeutral.setText(s);
        return this;
    }

    public MaterialDialog neutralButton(@StringRes int s, final ButtonClick buttonClick) {
        buttonNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick.onClick(MaterialDialog.this, v);
            }
        });
        buttonNeutral.setVisibility(View.VISIBLE);
        buttonNeutral.setText(s);
        return this;
    }

    public MaterialDialog setTitle(String s) {
        textViewTitle.setText(s);
        return this;
    }

    public void setTitle(@StringRes int s) {
        textViewTitle.setText(s);
    }

    public MaterialDialog setDescription(String s) {
        textViewDescription.setText(s);
        return this;
    }

    public MaterialDialog setDescription(@StringRes int i) {
        textViewDescription.setText(i);
        return this;
    }

    public interface ButtonClick {
        void onClick(MaterialDialog dialog, View v);
    }
}