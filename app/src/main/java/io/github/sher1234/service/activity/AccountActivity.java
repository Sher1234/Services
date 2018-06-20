package io.github.sher1234.service.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.util.NavigationIconClickListener;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private TextInputEditText editText4;
    private TextInputEditText editText5;
    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;
    private View linearLayout1;
    private View linearLayout2;
    private View progressView;
    private CheckBox checkBox;
    private View layout1;
    private View layout2;
    private View button;

    private PasswordChangeTask passwordTask;
    private AccountUpdateTask accountTask;

    private boolean exit = false;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(this,
                findViewById(R.id.coordinatorLayout),
                new AccelerateDecelerateInterpolator(),
                getResources().getDrawable(R.drawable.ic_menu),
                getResources().getDrawable(R.drawable.ic_close)));

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setSelected(true);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);

        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout2 = findViewById(R.id.linearLayout2);
        progressView = findViewById(R.id.progressView);
        imageView = findViewById(R.id.imageView2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        layout1 = findViewById(R.id.editView1);
        layout2 = findViewById(R.id.editView2);
        checkBox = findViewById(R.id.checkBox);
        button = findViewById(R.id.button9);

        findViewById(R.id.imageView2).setOnClickListener(this); // UpdateAccount View -
        findViewById(R.id.button10).setOnClickListener(this); // ChangePassword Cancel -
        findViewById(R.id.button11).setOnClickListener(this); // Change Password
        findViewById(R.id.button7).setOnClickListener(this); // Update Account Cancel -
        findViewById(R.id.button8).setOnClickListener(this); // Update Account
        findViewById(R.id.button9).setOnClickListener(this); // ChangePassword View -

        onResetViews();
    }

    private void onResetViews() {
        user = getUserFromPreferences();
        checkBox.setChecked(false);
        editText1.setText(user.getPhone());
        editText2.setText(user.getEmployeeID());
        editText1.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setText("");
        editText4.setText("");
        editText5.setText("");
        showView(false, linearLayout1, linearLayout2);
        showView(true, layout1, layout2, button, imageView);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onStart() {
        super.onStart();
        user = getUserFromPreferences();
        textView1.setText(user.getName());
        textView2.setText(user.getEmail());
        editText1.setText(user.getPhone());
        editText2.setText(user.getEmployeeID());
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        }
        exit = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exit = false;
            }
        }, 1500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                break;

            case R.id.button2:
                startActivity(new Intent(this, CallsActivity.class));
                finish();
                break;

            case R.id.button3:
                startActivity(new Intent(this, RegCallActivity.class));
                finish();
                break;

            case R.id.button4:
                break;

            case R.id.button5:
                break;

            case R.id.button6:
                onLogoutUser();
                break;

            case R.id.button8:
                if (isPhoneInvalid()) {
                    Toast.makeText(this, "Invalid Phone", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (isTextInvalid(editText2, 3)) {
                    Toast.makeText(this, "Invalid Employee ID", Toast.LENGTH_SHORT).show();
                    break;
                }
                user.setPhone(getString(editText1));
                user.setEmployeeID(getString(editText2));
                if (passwordTask != null)
                    passwordTask.cancel(true);
                if (accountTask != null)
                    accountTask.cancel(true);
                accountTask = new AccountUpdateTask(user);
                accountTask.execute();
                break;

            case R.id.button9:
                onResetViews();
                showView(false, button, layout1);
                showView(true, linearLayout2);
                break;

            case R.id.button7:
            case R.id.button10:
                onResetViews();
                break;

            case R.id.button11:
                if (isPasswordMismatch()) {
                    Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    break;
                }
                user.setPhone(getString(editText1));
                user.setEmployeeID(getString(editText2));
                if (accountTask != null)
                    accountTask.cancel(true);
                if (passwordTask != null)
                    passwordTask.cancel(true);
                passwordTask = new PasswordChangeTask(user.getEmail(), getString(editText3), getString(editText4));
                passwordTask.execute();
                break;

            case R.id.imageView2:
                onResetViews();
                showView(false, imageView, layout2);
                showView(true, linearLayout1);
                editText1.setEnabled(true);
                editText2.setEnabled(true);
                break;
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void onLogoutUser() {
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
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    private User getUserFromPreferences() {
        SharedPreferences preferences = AppController.getInstance()
                .getSharedPreferences(Strings.UserPreferences, Context.MODE_PRIVATE);
        User user = new User();
        user.setName(preferences.getString("Name", null));
        user.setEmail(preferences.getString("Email", null));
        user.setPhone(preferences.getString("Phone", null));
        user.setAdmin(preferences.getBoolean("IsAdmin", false));
        user.setPassword(preferences.getString("Password", null));
        user.setEmployeeID(preferences.getString("EmployeeID", null));
        return user;
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = AppController.getInstance().getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void showView(final boolean show, @NotNull View... views) {
        int shortAnimTime = AppController.getInstance().getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
        for (final View v : views) {
            v.setVisibility(show ? View.VISIBLE : View.GONE);
            v.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    private boolean isTextInvalid(TextInputEditText editText, int size) {
        return getString(editText).length() <= size;
    }

    private boolean isPhoneInvalid() {
        return getString(editText1).length() != 10;
    }

    @NonNull
    private String getString(@NotNull TextInputEditText editText) {
        if (editText.getText() == null)
            return "";
        else
            return editText.getText().toString();
    }

    private boolean isPasswordMismatch() {
        return !getString(editText4).equals(getString(editText5)) || isTextInvalid(editText4, 7);
    }

    private void onAccountUpdate(@NotNull User user) {
        onResetViews();
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
    class AccountUpdateTask extends AsyncTask<Void, Void, Boolean> {
        private final User user;
        private Users u;
        private int i = 4869;

        AccountUpdateTask(User user) {
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
                        u = response.body();
                        i = u.getResponse().getCode();
                    } else {
                        u = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    u = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    u = null;
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
            if (u.getResponse() != null) {
                Toast.makeText(AccountActivity.this, u.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                if (u.getResponse().getCode() == 1)
                    onAccountUpdate(user);
            } else
                Toast.makeText(AccountActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class PasswordChangeTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String oldPassword;
        private final String newPassword;

        private Users users;
        private int i = 4869;

        PasswordChangeTask(String email, String oldPassword, String newPassword) {
            this.email = email;
            this.newPassword = newPassword;
            this.oldPassword = oldPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            users = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Users> call = api.ChangePassword(email, oldPassword, newPassword);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        users = response.body();
                        i = users.getResponse().getCode();
                    } else {
                        users = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                    users = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    users = null;
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
            if (users != null) {
                Toast.makeText(AppController.getInstance(), users.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                if (users.getResponse().getCode() == 1) {
                    user.setPassword(getString(editText4));
                    onAccountUpdate(user);
                }
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}