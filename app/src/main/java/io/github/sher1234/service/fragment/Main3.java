package io.github.sher1234.service.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.api.File;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.github.sher1234.service.activity.MainActivity.F_TAG;

public class Main3 extends Fragment implements View.OnClickListener {

    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private View mProgressView;

    private RegisterTask task;

    public Main3() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_3, container, false);
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
        private io.github.sher1234.service.model.base.Response response;
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
            assert getActivity().getApplicationContext() != null;
            SharedPreferences preferences = getActivity()
                    .getSharedPreferences(File.UserPreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("status", true);
            editor.putString("Email", user.getEmail());
            editor.putString("Password", user.getPassword());
            editor.putString("Name", user.getName());
            editor.putString("Phone", user.getPhone());
            editor.putBoolean("IsAdmin", user.isAdmin());
            editor.putString("EmployeeID", user.getEmployeeID());
            if (!user.isExists()) {
                editor.putBoolean("exists", false);
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                Fragment outFragment = getFragmentManager().findFragmentByTag(F_TAG);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left,
                        R.anim.enter_left, R.anim.exit_right);
                if (outFragment != null)
                    fragmentTransaction.remove(outFragment);
                fragmentTransaction.replace(R.id.frameLayout, Main5.newInstance(user), F_TAG);
                fragmentTransaction.addToBackStack(F_TAG).commit();
            } else {
                editor.putBoolean("exists", true);
            }
            editor.apply();
        }
    }
}