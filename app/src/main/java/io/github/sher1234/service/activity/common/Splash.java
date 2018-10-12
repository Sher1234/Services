package io.github.sher1234.service.activity.common;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.Login;
import io.github.sher1234.service.fragment.Register;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.UserR;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.NavigateActivity;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Splash extends AppCompatActivity
        implements View.OnClickListener, NavigateActivity {

    private LoginTask task;
    private View mButtonView;
    private Snackbar snackbar;
    private View mProgressView;

    @Override
    protected void onStart() {
        super.onStart();
        permissionRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @Override
    public void onClick(View view) {
        BottomSheetDialogFragment fragment;
        switch (view.getId()) {
            case R.id.button1:
                fragment = new Login();
                fragment.showNow(getSupportFragmentManager(), Strings.TAG + "L");
                break;

            case R.id.button2:
                fragment = new Register();
                fragment.showNow(getSupportFragmentManager(), Strings.TAG + "R");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        mProgressView = findViewById(R.id.progressView);
        mButtonView = findViewById(R.id.buttonView);
        showProgress(true);
        onCheckUser();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == 4869) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Permission Error", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    public void onLoginDialog(User user) {
        AppController.setUserInPrefs(user);
        final MaterialDialog materialDialog = new MaterialDialog(this)
                .setTitle("Account Disabled")
                .setDescription(R.string.acc_disabled);
        materialDialog.positiveButton(R.string.sign_out, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.removeUserFromPrefs();
                finish();
            }
        }).negativeButton(R.string.update_account, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToActivity(AccountSettings.class, true);
            }
        }).neutralButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).setCancelable(false);
        materialDialog.show();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = AppController.getInstance().getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        mButtonView.setVisibility(!show ? View.VISIBLE : View.GONE);
        mButtonView.animate().setDuration(shortAnimTime).alpha(
                !show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mButtonView.setVisibility(!show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void permissionRequest() {
        String[] strings;
        strings = new String[5];
        strings[0] = Manifest.permission.INTERNET;
        strings[1] = Manifest.permission.ACCESS_FINE_LOCATION;
        strings[2] = Manifest.permission.ACCESS_COARSE_LOCATION;
        strings[3] = Manifest.permission.READ_EXTERNAL_STORAGE;
        strings[4] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, strings, 4869);
    }

    private void onCheckUser() {
        if (snackbar != null)
            snackbar.dismiss();
        User user = AppController.getUserFromPrefs();
        if (user.EmployeeID != null && user.Password != null) {
            if (task != null)
                task.cancel(true);
            task = null;
            task = new LoginTask(user.EmployeeID, user.Password);
            task.execute();
        } else
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showProgress(false);
                }
            }, 500);
    }

    @Override
    public void navigateToActivity(@NotNull Class c, boolean finish) {
        Intent intent = new Intent(this, c);
        intent.putExtra(Strings.ExtraData, true);
        startActivity(intent);
        if (finish)
            this.finish();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mPassword;
        private final String mEmail;
        private int i = 4869;
        private UserR user_r;

        LoginTask(String mEmail, String mPassword) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
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
                        user_r = response.body();
                        i = user_r.Code;
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
            if (user_r != null) {
                Toast.makeText(Splash.this, user_r.Message, Toast.LENGTH_SHORT).show();
                if (user_r.Code == 1)
                    onLogin(user_r.User);
            } else {
                if (i == 306)
                    snackbar = Snackbar.make(mProgressView, "Content parse error...", Snackbar.LENGTH_INDEFINITE);
                else if (i == 307)
                    snackbar = Snackbar.make(mProgressView, "Network failure...", Snackbar.LENGTH_INDEFINITE);
                else if (i == 308)
                    snackbar = Snackbar.make(mProgressView, "Request cancelled...", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCheckUser();
                    }
                }).show();
            }
        }

        private void onLogin(User user) {
            AppController.setUserInPrefs(user);
            if (!user_r.User.isRegistered()) {
                onLoginDialog(user);
            } else {
                showProgress(false);
                navigateToActivity(DashBoard.class, true);
            }
        }
    }
}