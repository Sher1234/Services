package io.github.sher1234.service.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import io.github.sher1234.service.Api;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.response.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResetPassword extends Fragment {

    private ResetPasswordTask checkUserTask;

    private EditText mOTP;
    private EditText mPassword1;
    private EditText mPassword2;
    private View mProgressView;

    private String email;
    private String otp;

    static final String PARAM_EMAIL = "PARAM-EMAIL";

    public ResetPassword() {

    }

    @NonNull
    public static ResetPassword newInstance(String email, String s) {
        ResetPassword resetPassword =  new ResetPassword();
        Bundle bundle = new Bundle();
        bundle.putString(Login.PARAM_OTP, s);
        bundle.putString(PARAM_EMAIL, email);
        resetPassword.setArguments(bundle);
        return resetPassword;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            otp = getArguments().getString(Login.PARAM_OTP, null);
            email = getArguments().getString(PARAM_EMAIL, null);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_start, container, false);
        mOTP = view.findViewById(R.id.textInputEditText1);
        mPassword1 = view.findViewById(R.id.textInputEditText2);
        mPassword2 = view.findViewById(R.id.textInputEditText3);
        mProgressView = view.findViewById(R.id.progressView);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.requestFocus();
                if (getContext() != null) {
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert inputMethodManager != null;
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                onResetPassword();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void onResetPassword() {
        if (checkUserTask != null)
            return;
        mOTP.setError(null);
        String otp = mOTP.getText().toString();
        mPassword1.setError(null);
        String password1 = mPassword1.getText().toString();
        mPassword1.setError(null);
        String password2 = mPassword2.getText().toString();
        View view = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(otp) || otp.length() < 4 || !otp.equalsIgnoreCase(this.otp)) {
            mOTP.setError("OTP Mismatch");
            view = mOTP;
            cancel = true;
        }
        if (TextUtils.isEmpty(password1) || password1.length() < 8) {
            mPassword1.setError("Invalid Password");
            view = mPassword1;
            cancel = true;
        }
        if (TextUtils.isEmpty(password2) || !password1.equals(password2)) {
            mPassword2.setError("Invalid Password");
            view = mPassword2;
            cancel = true;
        }
        if (cancel)
            view.requestFocus();
        else {
            showProgress(true);
            checkUserTask = new ResetPasswordTask(email, password1);
            checkUserTask.execute((Void) null);
        }
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
    class ResetPasswordTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private io.github.sher1234.service.model.base.Response response;
        private StringBuilder A = new StringBuilder();

        ResetPasswordTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.ResetPassword(mEmail, mPassword);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        A.append("Message: ").append(response.body().getResponse().getMessage()).append("\n");
                        A.append("Code: ").append(response.body().getResponse().getCode()).append("\n");
                        ResetPasswordTask.this.response = response.body().getResponse();
                    } else {
                        ResetPasswordTask.this.response = null;
                        A.append(response.toString()).append("\n").append("Error Processing Request");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    response = null;
                    A.append(t.getMessage());
                }
            });
            while (true)
                if (!A.toString().isEmpty())
                    return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            checkUserTask = null;
            showProgress(false);
            Log.e("Start/Response/A", A.toString());
            if (response != null && !response.getMessage().contains("Error")) {
                if (response.getCode() == 1) {
                    FragmentManager fragmentManager = getFragmentManager();
                    assert fragmentManager != null;
                    fragmentManager.popBackStack();
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            checkUserTask = null;
            showProgress(false);
        }
    }
}
