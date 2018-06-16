package io.github.sher1234.service.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.MainActivity;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.NavigationHost;
import io.github.sher1234.service.util.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Start2 extends Fragment implements View.OnClickListener {

    private TextInputEditText editText1;
    private TextInputEditText editText2;

    private ResetTask resetTask;
    private LoginTask loginTask;

    private View mProgressView;

    public Start2() {
        resetTask = null;
        loginTask = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_2, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        mProgressView = view.findViewById(R.id.progressView);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                if (isEmailValid()) {
                    Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    break;
                }
                resetPasswordRequest(getString(editText1));
                break;

            case R.id.button2:
                if (isEmailValid()) {
                    Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isPasswordValid()) {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (resetTask != null)
                    resetTask.cancel(true);
                if (loginTask != null)
                    loginTask.cancel(true);
                loginTask = new LoginTask(getString(editText1), getString(editText2));
                loginTask.execute();
                break;
        }
    }

    private boolean isEmailValid() {
        return getString(editText1).isEmpty()
                || getString(editText1).length() <= 5
                || !getString(editText1).contains("@");
    }

    private boolean isPasswordValid() {
        return getString(editText2).length() <= 7;
    }

    @NonNull
    private String getString(@NotNull TextInputEditText editText) {
        if (editText.getText() == null)
            return "";
        else
            return editText.getText().toString();
    }

    private void resetPasswordRequest(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Reset Password")
                .setMessage("An OTP will be sent to the given Email. Proceed if required")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (resetTask != null)
                            resetTask.cancel(true);
                        if (loginTask != null)
                            loginTask.cancel(true);
                        resetTask = new ResetTask(s);
                        resetTask.execute();
                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true);
        builder.create().show();
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

    @SuppressLint("StaticFieldLeak")
    private class ResetTask extends AsyncTask<Void, Void, Boolean> {
        private io.github.sher1234.service.model.base.Response response;
        private int i = 4869;
        private String mEmail;

        ResetTask(String mEmail) {
            this.mEmail = mEmail;
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
            Call<Users> call = api.ResetRequest(mEmail);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        ResetTask.this.response = response.body().getResponse();
                        i = ResetTask.this.response.getCode();
                    }
                    else {
                        ResetTask.this.response = null;
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
            if (response.getCode() == 1) {
                assert getActivity() != null;
                ((NavigationHost) getActivity()).navigateTo(Start4.newInstance(mEmail), true);
            } else {
                if (response != null)
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private io.github.sher1234.service.model.base.Response response;
        private User user;
        private int i = 4869;
        private String mEmail;
        private String mPassword;

        LoginTask(String mEmail, String mPassword) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
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
            Call<Users> call = api.Login(mEmail, mPassword);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        LoginTask.this.response = response.body().getResponse();
                        if (response.body().getUsers() != null && response.body().getUsers().size() == 1)
                            LoginTask.this.user = response.body().getUsers().get(0);
                        i = LoginTask.this.response.getCode();
                    }
                    else {
                        LoginTask.this.response = null;
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
                    onLogin();
            } else
                Toast.makeText(getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("CommitPrefEdits")
        private void onLogin() {
            assert getActivity() != null;
            ((UserPreferences) getActivity()).updateUserPreferences(user);
            if (!user.isExists()) {
                ((NavigationHost) getActivity()).navigateTo(Start5.newInstance(user), false);
            } else {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }
    }
}