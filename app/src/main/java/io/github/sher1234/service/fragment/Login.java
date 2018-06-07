package io.github.sher1234.service.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.Api;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.github.sher1234.service.activity.Login.F_TAG;

public class Login extends Fragment {

    static final String PARAM_OTP = "OTP_FRAG";

    private AsyncTask mAuthTask;

    private User user;

    private TextView mName;
    private TextView mEmail;
    private EditText mPassword;
    private View mProgressView;

    public Login() {

    }

    @NonNull
    public static Start newInstance(User u) {
        Start start = new Start();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Start.ARG_USER, u);
        start.setArguments(bundle);
        return start;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            user = (User) getArguments().getSerializable(Start.ARG_USER);
        else
            user = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_login, container, false);
        mName = view.findViewById(R.id.textView1);
        mEmail = view.findViewById(R.id.textView2);
        mProgressView = view.findViewById(R.id.progressView);
        mPassword = view.findViewById(R.id.textInputEditText);

        if (user.getName() == null || user.getName().isEmpty())
            mName.setVisibility(View.GONE);
        else
            mName.setText(user.getName());
        mEmail.setText(user.getEmail());

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
                onLoginUser();
            }
        });

        view.findViewById(R.id.buttonR).setOnClickListener(new View.OnClickListener() {
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
        if (mAuthTask != null)
            return;
        assert mEmail.getText() != null;
        String email = mEmail.getText().toString();
        boolean cancel = false;
        Log.e("logging.123/e", email + "ABC");
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgress(true);
        mAuthTask = new ResetPasswordTask(email);
        mAuthTask.execute((Void) null);
    }

    private void onLoginUser() {
        if (mAuthTask != null)
            return;
        mPassword.setError(null);
        assert mEmail.getText() != null;
        String email = mEmail.getText().toString();
        assert mPassword.getText() != null;
        String password = mPassword.getText().toString();
        boolean cancel = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError("Invalid Password");
            cancel = true;
        }
        if (cancel)
            mPassword.requestFocus();
        else {
            showProgress(true);
            mAuthTask = new LoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(@NotNull String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(@NotNull String password) {
        return password.length() > 7;
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
    class LoginTask extends AsyncTask<Void, Void, Boolean> {

        User u;
        User user = null;
        io.github.sher1234.service.model.base.Response response;

        StringBuilder A = new StringBuilder();

        LoginTask(String s1, String s2) {
            u = new User("", "", "", s1, s2, "");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            user = null;
            response = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.LoginUser(u.getEmail(), user.getPassword());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        A.append("Message: ").append(response.body().getResponse().getMessage()).append("\n");
                        A.append("Code: ").append(response.body().getResponse().getCode()).append("\n");
                        user = null;
                        LoginTask.this.response = response.body().getResponse();
                        if (response.body().getUsers().size() < 1)
                            return;
                        user = response.body().getUsers().get(0);
                    } else {
                        LoginTask.this.response = null;
                        user = null;
                        A.append(response.toString()).append("\n").append("No Response");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    LoginTask.this.response = null;
                    user = null;
                    A.append(t.getMessage());
                }
            });
            while (true)
                if (!A.toString().isEmpty())
                    return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mAuthTask = null;
            showProgress(false);
            Log.e("Login/Response/A", A.toString());
            if (user != null && response != null) {
                assert getActivity() != null;
                if (response.getCode() == 1 && user.isExists()) {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                    updateLogin(user);
                    // startActivity(new Intent(Login2.this, AllCalls.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Complete Your Account Details", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(Login2.this, AccountDetail.class));
                    getActivity().finish();
                }
            } else {
                Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }

        @SuppressLint("CommitPrefEdits")
        private void updateLogin(@NotNull User user) {
            assert getActivity() != null;
            SharedPreferences preferences = getActivity()
                    .getSharedPreferences("io.github.sher1234.service.user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("LoggedIn", true);
            editor.putString("Email", user.getEmail());
            editor.putString("Password", user.getPassword());
            editor.putString("Name", user.getName());
            editor.putString("Phone", user.getPhone());
            editor.putString("UserID", user.getUserID());
            editor.putString("EmployeeID", user.getEmployeeID());
            editor.apply();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ResetPasswordTask extends AsyncTask<Void, Void, Boolean> {

        String email;
        StringBuilder A = new StringBuilder();
        io.github.sher1234.service.model.base.Response response;

        ResetPasswordTask(String s) {
            email = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            response = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.ResetPasswordRequest(email);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        A.append("Message: ").append(response.body().getResponse().getMessage()).append("\n");
                        A.append("Code: ").append(response.body().getResponse().getCode()).append("\n");
                        ResetPasswordTask.this.response = response.body().getResponse();
                    } else {
                        ResetPasswordTask.this.response = null;
                        A.append(response.toString()).append("\n").append("No Response");
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
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mAuthTask = null;
            showProgress(false);
            Log.e("Login/Response/A", A.toString());
            if (response != null) {
                assert getActivity() != null;
                if (response.getCode() == 1 && response.getMessage().contains("OTP")) {
                    String[] s = response.getMessage().split(":");
                    String string = s[s.length -1];
                    Toast.makeText(getContext(), "OTP Sent to Your Email", Toast.LENGTH_SHORT).show();
                    assert getFragmentManager() != null;
                    Fragment outFragment = getFragmentManager().findFragmentByTag(F_TAG);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    if (outFragment != null)
                        fragmentTransaction.remove(outFragment);
                    fragmentTransaction.replace(R.id.frameLayout, ResetPassword.newInstance(email, string), F_TAG);
                    fragmentTransaction.addToBackStack(F_TAG).commit();
                } else {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
