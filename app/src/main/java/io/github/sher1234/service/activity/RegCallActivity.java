package io.github.sher1234.service.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.admin.AdminActivity;
import io.github.sher1234.service.activity.admin.UsersActivity;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.util.NavigationIconClickListener;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.listener.OnFormElementValueChangedListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerSingle;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextPhone;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegCallActivity extends AppCompatActivity implements OnFormElementValueChangedListener,
        View.OnClickListener {

    private FormBuilder mFormBuilder;
    private MaterialButton button;
    private View mProgressView;
    private RegCallTask task;
    private boolean isAdmin;
    private String email;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regcall);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(this,
                findViewById(R.id.coordinatorLayout),
                new AccelerateDecelerateInterpolator(),
                getResources().getDrawable(R.drawable.ic_menu),
                getResources().getDrawable(R.drawable.ic_close)));

        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        isAdmin = preferences.getBoolean("IsAdmin", false);
        email = preferences.getString("Email", "");

        if (isAdmin)
            findViewById(R.id.button0).setVisibility(View.VISIBLE);

        button = findViewById(R.id.button8);
        mProgressView = findViewById(R.id.progressView);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, mRecyclerView, this);
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
        button.setStrokeColorResource(R.color.colorHeaderBackground);
        onCreateForm();
    }

    @SuppressLint("SimpleDateFormat")
    private void onCreateForm() {
        Calendar calendar = Calendar.getInstance();
        String s1, s2;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateFormatID);
            s2 = "MAC" + dateFormat.format(calendar.getTime());
            dateFormat = new SimpleDateFormat(Strings.DateFormatView);
            s1 = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            s1 = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            s2 = "MAC" + calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH) + 1) +
                    calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR_OF_DAY) +
                    calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) +
                    calendar.get(Calendar.MILLISECOND);
            e.printStackTrace();
        }
        date = calendar.getTime();

        List<BaseFormElement> formItems = new ArrayList<>();

        List<String> op1 = new ArrayList<>();
        op1.add("Complaint");
        op1.add("New Commissioning");

        List<String> op2 = new ArrayList<>();
        op2.add("Yes");
        op2.add("No");
        op2.add("To be checked");

        BaseFormElement element0, element1, element2, element3;

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name")
                .setHint("Enter Name").setRequired(true).setTag(486901);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details")
                .setHint("Enter Site Details").setRequired(true).setTag(486902);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Concern Person
        element0 = FormHeader.createInstance("Concern Person");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name")
                .setHint("Enter Name").setRequired(true).setTag(486903);
        element2 = FormElementTextPhone.createInstance().setTitle("Mobile Number")
                .setHint("Enter Mobile").setRequired(true).setTag(486904);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Product Details
        element0 = FormHeader.createInstance("Product Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Detail/Info")
                .setHint("Enter Detail").setRequired(true).setTag(486905);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Serial Number")
                .setHint("Enter SNo").setRequired(false).setTag(486906);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Complaint Details
        element0 = FormHeader.createInstance("Complaint Details");
        element1 = FormElementPickerSingle.createInstance().setTitle("Nature of Site")
                .setOptions(op1).setRequired(true).setHint("Select One").setTag(486907);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Type of Complaint")
                .setHint("Enter Type").setRequired(true).setTag(486908);
        element3 = FormElementPickerSingle.createInstance().setTitle("Warranty")
                .setOptions(op2).setHint("Select One").setRequired(true).setTag(486909);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Call Number")
                .setValue(s2).setRequired(true).setTag(486910).setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Date/Time")
                .setValue(s1).setRequired(true).setTag(486911).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        mFormBuilder.addFormElements(formItems);
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

    @NotNull
    private Registration getRegistration() {
        return new Registration(0, getFormValue(486902), getFormValue(486901),
                getFormValue(486907), getFormValue(486905), getFormValue(486906),
                getFormValue(486908), getFormValue(486909), date, email, getFormValue(486910),
                getFormValue(486903), getFormValue(486904), false);
    }

    public String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    public void resetFormValue(int i) {
        mFormBuilder.getFormElement(i).setValue("");
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        if (getFormValue(486902).length() > 2 && getFormValue(486901).length() > 2 &&
                getFormValue(486907).length() > 2 && getFormValue(486905).length() > 2 &&
                getFormValue(486908).length() > 2 && getFormValue(486909).length() > 1 &&
                date != null && email.contains("@") && getFormValue(486910).length() > 5 &&
                getFormValue(486903).length() > 2 && getFormValue(486904).length() == 10 &&
                mFormBuilder.isValidForm()) {
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setStrokeColorResource(R.color.colorPrimaryDark);
        } else {
            button.setEnabled(false);
            button.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            button.setStrokeColorResource(R.color.colorHeaderBackground);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                if (isAdmin)
                    startActivity(new Intent(this, AdminActivity.class));
                else
                    startActivity(new Intent(this, DashboardActivity.class));
                finish();
                break;

            case R.id.button2:
                if (isAdmin)
                    startActivity(new Intent(this, io.github.sher1234.service.activity.admin.CallsActivity.class));
                else
                    startActivity(new Intent(this, CallsActivity.class));
                finish();
                break;

            case R.id.button0:
                startActivity(new Intent(this, UsersActivity.class));
                break;

            case R.id.button4:
                startActivity(new Intent(this, AccountActivity.class));
                finish();
                break;

            case R.id.button5:
                //About App
                break;

            case R.id.button6:
                onLogoutUser();
                break;

            case R.id.button7:
                onFormReset();
                break;

            case R.id.button8:
                if (task != null)
                    task.cancel(true);
                task = new RegCallTask(getRegistration());
                task.execute();
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

    @SuppressLint("SimpleDateFormat")
    private void onFormReset() {
        Calendar calendar = Calendar.getInstance();
        String s1, s2;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateFormatID);
            s2 = "MAC" + dateFormat.format(calendar.getTime());
            dateFormat = new SimpleDateFormat(Strings.DateFormatView);
            s1 = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            s1 = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            s2 = "MAC" + calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH) + 1) +
                    calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR_OF_DAY) +
                    calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) +
                    calendar.get(Calendar.MILLISECOND);
            e.printStackTrace();
        }
        date = calendar.getTime();
        for (int i = 486901; i <= 486911; i++)
            resetFormValue(i);
        mFormBuilder.getFormElement(486910).setValue(s2);
        mFormBuilder.getFormElement(486911).setValue(s1);
        mFormBuilder.getmFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class RegCallTask extends AsyncTask<Void, Void, Boolean> {

        private Registration registration;
        private Responded responded;
        private int i = 4869;

        RegCallTask(@NotNull Registration registration) {
            this.registration = registration;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api queryApi = retrofit.create(Api.class);
            Call<Responded> call = queryApi.RegisterCall(registration.getRegistrationMap());
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        Log.e("r/m/e", response.message() +
                                "\n" + response.toString() + "\n" +
                                response.code() + "\n" + response.body().getResponse().getMessage());
                        responded = response.body();
                        i = responded.getResponse().getCode();
                    } else {
                        responded = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Responded> call, @NonNull Throwable t) {
                    responded = null;
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    responded = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if (responded != null) {
                Toast.makeText(AppController.getInstance(), responded.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                if (responded.getResponse().getCode() == 1)
                    RegCallActivity.this.finish();
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}