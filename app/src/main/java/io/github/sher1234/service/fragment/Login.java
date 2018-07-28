package io.github.sher1234.service.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.DashBoard;
import io.github.sher1234.service.activity.common.Splash;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.UserR;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.NavigateActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends BottomSheetDialogFragment implements View.OnClickListener, TextWatcher {

    private final Functions functions = new Functions();
    private TextInputLayout inputLayout1;
    private TextInputLayout inputLayout2;
    private TextInputEditText editText1;
    private TextInputEditText editText2;

    private LoginTask task;

    private View mProgressView;
    private View mFormView;

    public Login() {
        task = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_login, container, false);
        view.findViewById(R.id.imageView).setOnClickListener(this);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        inputLayout1 = view.findViewById(R.id.textInputLayout1);
        inputLayout2 = view.findViewById(R.id.textInputLayout2);
        mProgressView = view.findViewById(R.id.progressView);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        mFormView = view.findViewById(R.id.formView);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                dismissAllowingStateLoss();
                break;

            case R.id.button1:
                if (isUserIdInvalid()) {
                    inputLayout1.setError("Invalid e-Mail/Employee ID");
                    Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    break;
                }
                resetPasswordRequest();
                break;

            case R.id.button2:
                if (isUserIdInvalid()) {
                    inputLayout1.setError("Invalid e-Mail/Employee ID");
                    Toast.makeText(getContext(), "Invalid e-Mail/Employee ID...", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (functions.isPasswordInvalid(functions.getString(editText2))) {
                    inputLayout2.setError("Invalid password");
                    Toast.makeText(getContext(), "Invalid password...", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (task != null)
                    task.cancel(true);
                task = new LoginTask(functions.getString(editText1), functions.getString(editText2));
                task.execute();
                break;
        }
    }

    private boolean isUserIdInvalid() {
        return functions.isEmailInvalid(functions.getString(editText1)) &&
                functions.isEmployeeIdInvalid(functions.getString(editText1));
    }

    private void resetPasswordRequest() {
        assert getContext() != null;
        final MaterialDialog materialDialog = new MaterialDialog(getContext())
                .setTitle("Recover Account")
                .setDescription("Resetting account password is currently disabled. Contact " +
                        "system administrator for account recovery.");
        materialDialog.negativeButton("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        }).setCancelable(true);
        materialDialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputLayout1.setError(null);
        inputLayout2.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private UserR userR;
        private int i = 4869;

        LoginTask(String mEmail, String mPassword) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, mProgressView, mFormView);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<UserR> call = api.Login(mEmail, mPassword);
            call.enqueue(new Callback<UserR>() {
                @Override
                public void onResponse(@NonNull Call<UserR> call, @NonNull Response<UserR> response) {
                    if (response.body() != null) {
                        userR = response.body();
                        i = userR.Code;
                    } else {
                        userR = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserR> call, @NonNull Throwable t) {
                    userR = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    userR = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            functions.showProgress(false, mProgressView, mFormView);
            if (userR != null) {
                Toast.makeText(getContext(), userR.Message, Toast.LENGTH_SHORT).show();
                if (userR.Code == 1)
                    onLogin(userR.User);
            } else if (i == 306)
                Toast.makeText(AppController.getInstance(), "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(AppController.getInstance(), "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(AppController.getInstance(), "Request cancelled...", Toast.LENGTH_SHORT).show();
            dismissAllowingStateLoss();
        }

        private void onLogin(User user) {
            assert getActivity() != null;
            AppController.setUserInPrefs(user);
            if (!userR.User.isRegistered()) {
                ((Splash) getActivity()).onLoginDialog(user);
                dismissAllowingStateLoss();
            } else
                ((NavigateActivity) getActivity()).navigateToActivity(DashBoard.class, true);
        }
    }
}