package io.github.sher1234.service.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.sher1234.service.R;

@SuppressWarnings("all")
public class DialogX extends Dialog {

    private final Context context;
    private Button buttonPositive;
    private Button buttonNegative;
    private Button buttonNeutral;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private ImageView imageView;
    private LinearLayoutCompat linearLayout;

    private View viewDivider;

    public DialogX(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);
        this.context = context;
        setContentView(R.layout.dialog_layout);
        setViewElements();
    }

    private void setViewElements() {
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

    public DialogX positiveButton(String s, View.OnClickListener onClickListener) {
        buttonPositive.setText(s);
        buttonPositive.setOnClickListener(onClickListener);
        buttonPositive.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX positiveButton(@StringRes int s, View.OnClickListener onClickListener) {
        buttonPositive.setText(s);
        buttonPositive.setOnClickListener(onClickListener);
        buttonPositive.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX negativeButton(String s, View.OnClickListener onClickListener) {
        buttonNegative.setText(s);
        buttonNegative.setOnClickListener(onClickListener);
        buttonNegative.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX negativeButton(@StringRes int s, View.OnClickListener onClickListener) {
        buttonNegative.setText(s);
        buttonNegative.setOnClickListener(onClickListener);
        buttonNegative.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX neutralButton(String s, View.OnClickListener onClickListener) {
        buttonNeutral.setText(s);
        buttonNeutral.setOnClickListener(onClickListener);
        buttonNeutral.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    private void showDivider() {
        viewDivider.setVisibility(View.VISIBLE);
    }

    public DialogX setTitle(String s) {
        if (s.length() > 15)
            textViewTitle.setTextSize((float) 17.5);
        else
            textViewTitle.setTextSize(22);
        textViewTitle.setText(s);
        return this;
    }

    public DialogX setDescription(String s) {
        textViewDescription.setText(s);
        this.setDescriptionGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    public DialogX setDescription(@StringRes int i) {
        textViewDescription.setText(i);
        this.setDescriptionGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    public DialogX setDescriptionGravity(int i) {
        textViewDescription.setGravity(i);
        return this;
    }

    public DialogX setButtonOrientation(int orientation) {
        linearLayout.setOrientation(orientation);
        return this;
    }
}