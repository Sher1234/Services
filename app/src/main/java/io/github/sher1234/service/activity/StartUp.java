package io.github.sher1234.service.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class StartUp extends AppCompatActivity {
    private CheckUserTask checkUserTask = null;
    private static final int REQUEST_PERMISSIONS = 4869;

    private TextInputEditText mEmail;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmail = findViewById(R.id.textInputEditText1);
        mProgressView = findViewById(R.id.progressView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.requestFocus();
                InputMethodManager inputMethodManager =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onClickLogin();
            }
        });
        /*
        SharedPreferences preferences = getSharedPreferences("UserLog", Context.MODE_PRIVATE);
        if (preferences.getBoolean("io.github.sher1234.service.user", false)) {
            if (checkUserTask != null)
                checkUserTask.cancel(true);
            checkUserTask = null;
            checkUserTask = new CheckUserTask(preferences.getString("email", null));
            checkUserTask.execute();
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void onClickLogin() {
        if (checkUserTask != null)
            return;
        mEmail.setError(null);
        String email = mEmail.getText().toString();
        boolean cancel = false;
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }

        if (cancel)
            mEmail.requestFocus();
        else {
            showProgress(true);
            checkUserTask = new CheckUserTask(email);
            checkUserTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(@NotNull String email) {
        return email.contains("@");
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
    public class CheckUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private User user = null;
        private io.github.sher1234.service.model.base.Response response;
        private StringBuilder A = new StringBuilder();

        CheckUserTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.CheckUserEmail(mEmail);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        A.append("Message: ").append(response.body().getResponse().getMessage()).append("\n");
                        A.append("Code: ").append(response.body().getResponse().getCode()).append("\n");
                        user = null;
                        CheckUserTask.this.response = response.body().getResponse();
                        if (response.body().getUsers().size() < 1)
                            return;
                        user = response.body().getUsers().get(0);
                    } else {
                        CheckUserTask.this.response = null;
                        user = null;
                        A.append(response.toString()).append("\n").append("Error Processing Request");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    user = null;
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
            Log.e("StartUp/Response/A", A.toString());
            if (user != null && response != null) {
                if (response.getCode() == 1 && !user.getEmail().isEmpty()) {

                    //updateLogin(user);
                    //startActivity(new Intent(StartUp.this, AllCalls.class));
                    finish();
                } else {
                    //startActivity(new Intent(Login2.this, AccountDetail.class));
                    finish();
                }
            } else {
                //Login2.this.mPassword.setError("Invalid Credentials");
                //Login2.this.mPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            checkUserTask = null;
            showProgress(false);
        }
    }
}

