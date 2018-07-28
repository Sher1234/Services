package io.github.sher1234.service.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.ChangePassword;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.UserR;
import io.github.sher1234.service.util.BottomMenuListener;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.NavigateActivity;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountSettings extends AppCompatActivity implements View.OnClickListener, NavigateActivity,
        CompoundButton.OnCheckedChangeListener, Toolbar.OnMenuItemClickListener, TextWatcher {

    private final Functions functions = new Functions();
    private TextInputLayout inputLayout1, inputLayout2, inputLayout3;
    private boolean backed = false, exit = false, menu = false;
    private TextInputEditText editText1, editText2, editText3;
    private TextView textView1, textView2;
    private FloatingActionButton button;
    private BottomAppBar bottomAppBar;
    private AccountUpdateTask task;
    private View progressView;
    private CheckBox checkBox;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        menu = getIntent().getBooleanExtra(Strings.ExtraData, false);

        if (menu)
            bottomAppBar.setNavigationIcon(null);
        else
            bottomAppBar.setNavigationOnClickListener(
                    new BottomMenuListener(getSupportFragmentManager(), 5));

        inputLayout1 = findViewById(R.id.textInputLayout1);
        inputLayout2 = findViewById(R.id.textInputLayout2);
        inputLayout3 = findViewById(R.id.textInputLayout3);
        button = findViewById(R.id.floatingActionButton);
        progressView = findViewById(R.id.progressView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        checkBox = new CheckBox(this);

        button.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(this);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        onSetViews(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        }
        if (editText1.isEnabled()) {
            backed = true;
            onSetViews(false);
            checkBox.setChecked(false);
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
        if (view.getId() == R.id.floatingActionButton) {
            backed = false;
            checkBox.setChecked(!checkBox.isChecked());
        }
    }

    private void onSetViews(boolean b) {
        user = AppController.getUserFromPrefs();
        editText1.setEnabled(b);
        editText2.setEnabled(b);
        editText3.setFocusableInTouchMode(false);
        editText1.setText(user.Name);
        textView1.setText(user.Email);
        editText2.setText(user.Phone);
        editText3.setText(user.EmployeeID);
        textView2.setText(user.EmployeeID);
        if (b) {
            editText1.requestFocus();
            if (!menu)
                bottomAppBar.getMenu().clear();
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            button.setImageResource(R.drawable.ic_save);
        } else {
            textView1.requestFocus();
            if (!menu)
                bottomAppBar.replaceMenu(R.menu.account);
            button.setImageResource(R.drawable.ic_edit);
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        }
        bottomAppBar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menuPassword) {
            ChangePassword.newInstance().show(getSupportFragmentManager(), Strings.TAG);
            return true;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            user = AppController.getUserFromPrefs();
            user.Name = functions.getString(editText1);
            user.Phone = functions.getString(editText2);
            if (backed) {
                Toast.makeText(this, "Account edit cancelled...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (functions.isNameInvalid(user.Name)) {
                inputLayout1.setError("Invalid name");
                return;
            }
            if (functions.isPhoneInvalid(user.Phone)) {
                inputLayout2.setError("Invalid phone number");
                return;
            }
            if (functions.isEmployeeIdInvalid(user.EmployeeID)) {
                inputLayout3.setError("Invalid Employee-ID");
                return;
            }
            if (functions.isNameInvalid(user.Email)) {
                Toast.makeText(this, "Invalid email...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (task != null)
                task.cancel(true);
            task = null;
            task = new AccountUpdateTask(user);
            task.execute();
        } else
            onSetViews(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputLayout1.setError(null);
        inputLayout2.setError(null);
        inputLayout3.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void navigateToActivity(@NotNull Class c, boolean finish) {
        startActivity(new Intent(this, c));
        if (finish)
            finish();
    }

    @SuppressLint("StaticFieldLeak")
    class AccountUpdateTask extends AsyncTask<Void, Void, Boolean> {
        private final User user;
        private int i = 4869;
        private UserR u;

        AccountUpdateTask(User user) {
            this.user = user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, progressView, null);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<UserR> call = api.UpdateAccount(user.getUserMap());
            call.enqueue(new Callback<UserR>() {
                @Override
                public void onResponse(@NonNull Call<UserR> call, @NonNull Response<UserR> response) {
                    if (response.body() != null) {
                        u = response.body();
                        i = u.Code;
                        return;
                    }
                    u = null;
                    i = 306;
                }

                @Override
                public void onFailure(@NonNull Call<UserR> call, @NonNull Throwable t) {
                    u = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
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
            functions.showProgress(false, progressView, null);
            if (u != null) {
                Toast.makeText(AccountSettings.this, u.Message, Toast.LENGTH_SHORT).show();
                if (u.Code == 1)
                    AppController.setUserInPrefs(user);
            } else if (i == 306)
                Toast.makeText(AppController.getInstance(), "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(AppController.getInstance(), "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(AppController.getInstance(), "Request cancelled...", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}