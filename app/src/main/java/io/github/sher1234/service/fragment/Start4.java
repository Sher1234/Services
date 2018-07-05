package io.github.sher1234.service.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.response.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Start4 extends Fragment implements View.OnClickListener {

    private static final String PARAM_EMAIL = "PARAM_EMAIL-M4";

    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private TextInputEditText editText4;

    private View mProgressView;

    private String email;

    private ResetPasswordTask task;

    public Start4() {
    }

    @NonNull
    public static Fragment newInstance(String email) {
        Start4 start4 = new Start4();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_EMAIL, email);
        start4.setArguments(bundle);
        return start4;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_4, container, false);
        view.findViewById(R.id.button).setOnClickListener(this);
        mProgressView = view.findViewById(R.id.progressView);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        task = null;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText1.setText(email);
        editText1.setEnabled(false);
        editText2.requestFocus();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        email = getArguments().getString(PARAM_EMAIL, null);
    }

    private boolean isPasswordMismatch() {
        return !getString(editText3).equals(getString(editText4)) || getString(editText3).length() < 8;
    }

    private boolean isOtpInvalid() {
        return getString(editText2).length() < 3;
    }

    private boolean isEmailInvalid() {
        return getString(editText1).isEmpty()
                || getString(editText1).length() <= 5
                || !getString(editText1).contains("@");
    }

    @NonNull
    private String getString(@NotNull TextInputEditText editText) {
        if (editText.getText() == null)
            return "";
        else
            return editText.getText().toString();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (isEmailInvalid()) {
                    Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isOtpInvalid()) {
                    Toast.makeText(getContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isPasswordMismatch()) {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (task != null)
                    task.cancel(true);
                task = new ResetPasswordTask(getString(editText2), getString(editText1), getString(editText3));
                task.execute();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ResetPasswordTask extends AsyncTask<Void, Void, Boolean> {
        private Responded response;
        private final String otp;
        private final String email;
        private final String password;
        private int i = 4869;

        ResetPasswordTask(String otp, String email, String password) {
            this.otp = otp;
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.ResetPassword(otp, email, password);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        ResetPasswordTask.this.response = response.body().getResponse();
                        i = ResetPasswordTask.this.response.getCode();
                    }
                    else {
                        ResetPasswordTask.this.response = null;
                        i = -1;
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    response = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    response = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            if (response != null) {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                if (response.getCode() == 1)
                    onPasswordReset();
            } else
                Toast.makeText(getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
        }

        private void onPasswordReset() {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        }
    }
}
