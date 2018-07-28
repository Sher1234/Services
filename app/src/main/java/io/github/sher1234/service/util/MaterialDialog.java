package io.github.sher1234.service.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.sher1234.service.R;

@SuppressWarnings("all")
public class MaterialDialog extends Dialog {

    private View view;
    private ImageView imageView;
    private Button buttonNeutral;
    private final Context context;
    private Button buttonNegative;
    private Button buttonPositive;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private LinearLayoutCompat linearLayout;

    private View viewDivider;

    public MaterialDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);
        this.context = context;
        setContentView(R.layout.dialog_layout);
        setViewElements();
    }

    private void setViewElements() {
        view = findViewById(R.id.view);
        imageView = findViewById(R.id.imageViewBanner);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        linearLayout = findViewById(R.id.linearLayoutButton);
        buttonPositive = findViewById(R.id.buttonPositive);
        buttonNegative = findViewById(R.id.buttonNegative);
        buttonNeutral = findViewById(R.id.buttonNeutral);
        viewDivider = findViewById(R.id.viewDivider);
        buttonPositive.setVisibility(View.GONE);
        buttonNegative.setVisibility(View.GONE);
        buttonNeutral.setVisibility(View.GONE);
        viewDivider.setVisibility(View.GONE);
    }

    public MaterialDialog positiveButton(String s, View.OnClickListener onClickListener) {
        buttonPositive.setText(s);
        buttonPositive.setOnClickListener(onClickListener);
        buttonPositive.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public MaterialDialog positiveButton(@StringRes int s, View.OnClickListener onClickListener) {
        buttonPositive.setText(s);
        buttonPositive.setOnClickListener(onClickListener);
        buttonPositive.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public MaterialDialog negativeButton(String s, View.OnClickListener onClickListener) {
        buttonNegative.setText(s);
        buttonNegative.setOnClickListener(onClickListener);
        buttonNegative.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public MaterialDialog negativeButton(@StringRes int s, View.OnClickListener onClickListener) {
        buttonNegative.setText(s);
        buttonNegative.setOnClickListener(onClickListener);
        buttonNegative.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public MaterialDialog neutralButton(String s, View.OnClickListener onClickListener) {
        buttonNeutral.setText(s);
        buttonNeutral.setOnClickListener(onClickListener);
        buttonNeutral.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public MaterialDialog neutralButton(@StringRes int s, View.OnClickListener onClickListener) {
        buttonNeutral.setText(s);
        buttonNeutral.setOnClickListener(onClickListener);
        buttonNeutral.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    private void showDivider() {
        viewDivider.setVisibility(View.VISIBLE);
    }

    public MaterialDialog setTitle(String s) {
        if (s.length() > 15)
            textViewTitle.setTextSize((float) 17.5);
        else
            textViewTitle.setTextSize(22);
        textViewTitle.setText(s);
        return this;
    }

    public void setTitle(@StringRes int s) {
        if (context.getResources().getString(s).length() > 15)
            textViewTitle.setTextSize((float) 17.5);
        else
            textViewTitle.setTextSize(22);
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

    public MaterialDialog setButtonOrientation(int orientation) {
        linearLayout.setOrientation(orientation);
        if (orientation == LinearLayoutCompat.VERTICAL)
            view.setVisibility(View.GONE);
        return this;
    }
}