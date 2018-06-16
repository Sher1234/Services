package io.github.sher1234.service.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.MainActivity;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Start5 extends Fragment implements View.OnClickListener, CheckBox.OnCheckedChangeListener {
    private static final String PARAM_USER = "PARAM_USER-M5";

    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private TextInputEditText editText4;
    private View mProgressView;

    private UpdateAccountTask task;

    private User user;

    public Start5() {
    }

    @NonNull
    public static Fragment newInstance(User user) {
        Start5 main4 = new Start5();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_USER, user);
        main4.setArguments(bundle);
        return main4;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_5, container, false);
        view.findViewById(R.id.button).setOnClickListener(this);
        mProgressView = view.findViewById(R.id.progressView);
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        task = null;
        checkBox.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (user.getName() != null)
            editText1.setText(user.getName());
        editText2.setText(user.getEmail());
        if (user.getPhone() != null)
            editText3.setText(user.getPhone());
        if (user.getEmployeeID() != null)
            editText4.setText(user.getEmployeeID());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        user = (User) getArguments().getSerializable(PARAM_USER);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (isEmailInvalid()) {
                    Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isTextInvalid(editText1, 2)) {
                    Toast.makeText(getContext(), "Invalid Name", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isPhoneInvalid()) {
                    Toast.makeText(getContext(), "Invalid Phone", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isTextInvalid(editText4, 3)) {
                    Toast.makeText(getContext(), "Invalid Employee ID", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (task != null)
                    task.cancel(true);
                user.setName(getString(editText1));
                user.setPhone(getString(editText3));
                user.setEmployeeID(getString(editText4));
                task = new UpdateAccountTask(user);
                task.execute();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.checkBox) {
            if (b) {
                editText4.setText(editText2.getText());
                editText4.setEnabled(false);
            } else {
                editText4.setText("");
                editText4.setEnabled(true);
            }
        }
    }

    private boolean isTextInvalid(TextInputEditText editText, int size) {
        return getString(editText).length() <= size;
    }

    private boolean isPhoneInvalid() {
        return getString(editText3).length() != 10;
    }

    private boolean isEmailInvalid() {
        return getString(editText2).isEmpty()
                || getString(editText2).length() <= 5
                || !getString(editText2).contains("@");
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

    @SuppressLint("StaticFieldLeak")
    private class UpdateAccountTask extends AsyncTask<Void, Void, Boolean> {
        private io.github.sher1234.service.model.base.Response response;
        private final User user;
        private int i = 4869;

        UpdateAccountTask(User user) {
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
            Call<Users> call = api.UpdateAccount(user.getUserMap());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        UpdateAccountTask.this.response = response.body().getResponse();
                        i = UpdateAccountTask.this.response.getCode();
                    }
                    else {
                        UpdateAccountTask.this.response = null;
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
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }
}