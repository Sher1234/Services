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
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.NavigationHost;
import io.github.sher1234.service.util.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Start3 extends Fragment implements View.OnClickListener {

    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private View mProgressView;

    private RegisterTask task;

    public Start3() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_3, container, false);
        view.findViewById(R.id.button).setOnClickListener(this);
        mProgressView = view.findViewById(R.id.progressView);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        task = null;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText1.setText("");
        editText2.setText("");
        editText3.setText("");
    }

    private boolean isPasswordMismatch() {
        return !getString(editText2).equals(getString(editText3)) || getString(editText2).length() < 8;
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
                if (isPasswordMismatch()) {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (task != null)
                    task.cancel(true);
                User user = new User();
                user.setEmail(getString(editText1));
                user.setPassword(getString(editText2));
                task = new RegisterTask(user);
                task.execute();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {
        private Responded response;
        private final User user;
        private int i = 4869;

        RegisterTask(User user) {
            this.user = user;
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
            Call<Users> call = api.Register(user.getEmail(), user.getPassword());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        RegisterTask.this.response = response.body().getResponse();
                        i = RegisterTask.this.response.getCode();
                    }
                    else {
                        RegisterTask.this.response = null;
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
            ((NavigationHost) getActivity()).navigateTo(Start5.newInstance(user), false);
        }
    }
}