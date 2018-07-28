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

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.UserR;
import io.github.sher1234.service.util.Functions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePassword extends BottomSheetDialogFragment implements View.OnClickListener,
        TextWatcher {

    private final Functions functions = new Functions();
    private TextInputLayout inputLayout1;
    private TextInputLayout inputLayout2;
    private TextInputLayout inputLayout3;
    private TextInputLayout inputLayout4;
    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private TextInputEditText editText4;
    private View mProgressView;
    private View mFormView;

    private PasswordTask task;
    private User user;

    public ChangePassword() {
        task = null;
    }

    @NotNull
    public static ChangePassword newInstance() {
        return new ChangePassword();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = AppController.getUserFromPrefs();
        setCancelable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_password, container, false);
        view.findViewById(R.id.imageView).setOnClickListener(this);
        view.findViewById(R.id.button).setOnClickListener(this);
        inputLayout1 = view.findViewById(R.id.textInputLayout1);
        inputLayout2 = view.findViewById(R.id.textInputLayout2);
        inputLayout3 = view.findViewById(R.id.textInputLayout3);
        inputLayout4 = view.findViewById(R.id.textInputLayout4);
        mProgressView = view.findViewById(R.id.progressView);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        mFormView = view.findViewById(R.id.formView);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);
        task = null;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText2.setText(user.EmployeeID);
        editText1.setText(user.Email);
        editText3.setText("");
        editText4.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                dismissAllowingStateLoss();
                break;

            case R.id.button:
                if (functions.isEmailInvalid(functions.getString(editText1))) {
                    inputLayout1.setError("Invalid Email");
                    break;
                }
                if (functions.isEmployeeIdInvalid(functions.getString(editText2))) {
                    inputLayout2.setError("Invalid Employee ID");
                    break;
                }
                if (functions.isPasswordInvalid(functions.getString(editText3))) {
                    inputLayout3.setError("Invalid password");
                    break;
                }
                if (functions.isPasswordInvalid(functions.getString(editText4))) {
                    inputLayout4.setError("Invalid new password");
                    break;
                }
                if (task != null)
                    task.cancel(true);
                task = new PasswordTask(user.EmployeeID, functions.getString(editText3),
                        functions.getString(editText4));
                task.execute();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputLayout1.setError(null);
        inputLayout2.setError(null);
        inputLayout3.setError(null);
        inputLayout4.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @SuppressLint("StaticFieldLeak")
    private class PasswordTask extends AsyncTask<Void, Void, Boolean> {
        private final String email;
        private final String value;
        private final String password;
        private User user;
        private int i = 4869;
        private UserR user_r;

        PasswordTask(String email, String password, String value) {
            this.email = email;
            this.value = value;
            this.password = password;
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
            Call<UserR> call = api.ChangePassword(email, password, value);
            call.enqueue(new Callback<UserR>() {
                @Override
                public void onResponse(@NonNull Call<UserR> call, @NonNull Response<UserR> response) {
                    if (response.body() != null) {
                        user_r = response.body();
                        i = user_r.Code;
                        user = user_r.User;
                    } else {
                        user_r = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserR> call, @NonNull Throwable t) {
                    user_r = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    user_r = null;
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
            if (user_r != null) {
                Toast.makeText(getContext(), user_r.Message, Toast.LENGTH_SHORT).show();
                if (user_r.Code == 1)
                    AppController.setUserInPrefs(user);
            } else if (i == 306)
                Toast.makeText(AppController.getInstance(), "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(AppController.getInstance(), "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(AppController.getInstance(), "Request cancelled...", Toast.LENGTH_SHORT).show();
            dismissAllowingStateLoss();
        }
    }
}