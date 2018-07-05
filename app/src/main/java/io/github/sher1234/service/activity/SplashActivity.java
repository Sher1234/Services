package io.github.sher1234.service.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.admin.AdminActivity;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.DialogX;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity implements UserPreferences {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        onCheckUser();
    }

    private void onCheckUser() {
        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        User user = getUserFromPreferences(preferences);
        if (user.isExists()) {
            new LoginTask(user.getEmail(), user.getPassword()).execute();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                    finish();
                }
            }, 650);
        }
    }

    private User getUserFromPreferences(@NotNull SharedPreferences preferences) {
        User user = new User();
        user.setName(preferences.getString("Name", null));
        user.setEmail(preferences.getString("Email", null));
        user.setPhone(preferences.getString("Phone", null));
        user.setAdmin(preferences.getBoolean("IsAdmin", false));
        user.setPassword(preferences.getString("Password", null));
        user.setEmployeeID(preferences.getString("EmployeeID", null));
        return user;
    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionRequest();
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

    @Override
    @SuppressLint("CommitPrefEdits")
    public void updateUserPreferences(@NotNull User user) {
        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("status", true);
        editor.putString("Email", user.getEmail());
        editor.putString("Password", user.getPassword());
        editor.putString("Name", user.getName());
        editor.putString("Phone", user.getPhone());
        editor.putBoolean("IsAdmin", user.isAdmin());
        editor.putString("EmployeeID", user.getEmployeeID());
        editor.putBoolean("exists", user.isExists());
        editor.apply();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private Responded response;
        private User user;
        private int i = 4869;

        LoginTask(String mEmail, String mPassword) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
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
                    } else {
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
            if (response != null) {
                Toast.makeText(SplashActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                if (response.getCode() == 1)
                    onLogin();
                else {
                    final DialogX dialogX = new DialogX(SplashActivity.this)
                            .setTitle("Account Disabled")
                            .setDescription(R.string.acc_disabled);
                    dialogX.positiveButton(R.string.sign_out, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onSignOut();
                            dialogX.dismiss();
                        }
                    }).negativeButton(R.string.cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogX.dismiss();
                            finish();
                        }
                    }).setCancelable(false);
                    dialogX.show();
                }
            } else
                Toast.makeText(SplashActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("CommitPrefEdits")
        private void onSignOut() {
            SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("status", false);
            editor.putBoolean("exists", false);
            editor.remove("EmployeeID");
            editor.remove("Password");
            editor.remove("IsAdmin");
            editor.remove("Email");
            editor.remove("Phone");
            editor.remove("Name");
            editor.apply();
            startActivity(new Intent(SplashActivity.this, SplashActivity.class));
            finish();
        }

        @SuppressLint("CommitPrefEdits")
        private void onLogin() {
            updateUserPreferences(user);
            if (!user.isExists()) {
                startActivity(new Intent(SplashActivity.this, StartActivity.class));
                finish();
            } else {
                if (user.isAdmin())
                    startActivity(new Intent(SplashActivity.this, AdminActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                finish();
            }
        }
    }
}