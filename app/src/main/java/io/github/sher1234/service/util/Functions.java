package io.github.sher1234.service.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.sher1234.service.AppController;

public class Functions {
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

    public void showProgress(final boolean b, @NotNull final View view1,
                             @Nullable final View view2) {
        showProgress(b, view1);
        if (view2 != null)
            showProgress(!b, view2);
    }

    public void showProgress(final boolean b, @NotNull final View view) {
        int shortAnimTime = AppController.getInstance().getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
        view.setVisibility(b ? View.VISIBLE : View.GONE);
        view.animate().setDuration(shortAnimTime).alpha(
                b ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
    }
}