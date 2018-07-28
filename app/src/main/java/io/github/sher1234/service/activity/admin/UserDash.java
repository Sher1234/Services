package io.github.sher1234.service.activity.admin;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.CallsFrag;
import io.github.sher1234.service.fragment.DashFrag;
import io.github.sher1234.service.fragment.UserFrag;
import io.github.sher1234.service.model.base.Employee;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserDash extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final Functions functions = new Functions();
    private BottomNavigationView bottomNavigationView;
    private Dashboard dashboard;
    private DashboardTask task;
    private View mProgressView;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        employee = (Employee) getIntent().getSerializableExtra(Strings.ExtraData);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        mProgressView = findViewById(R.id.progressView);
        onStartTask();
    }

    private void onStartTask() {
        SimpleDateFormat format = new SimpleDateFormat(Strings.DateServer, Locale.US);
        Calendar calendar = Calendar.getInstance();
        String s = format.format(calendar.getTime());
        if (task != null)
            task.cancel(true);
        task = null;
        task = new DashboardTask(employee.Email, s, employee.EmployeeID);
        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dashboard) {
            onDataUpdate(DashFrag.newInstance(dashboard));
            return true;
        } else if (item.getItemId() == R.id.calls) {
            onDataUpdate(CallsFrag.newInstance(dashboard));
            return true;
        } else if (item.getItemId() == R.id.info) {
            onDataUpdate(UserFrag.newInstance(dashboard.user));
            return true;
        } else
            return false;
    }

    private void showProgress(boolean show) {
        functions.showProgress(show, mProgressView);
    }

    private void onDataUpdate(@NotNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment,
                        Strings.TAG + "U-D-A")
                .commit();
    }

    @SuppressLint("StaticFieldLeak")
    class DashboardTask extends AsyncTask<Void, Void, Boolean> {

        private final String date;
        private final String e_id;
        private final String email;
        private Dashboard dashboard;
        private int i = 4869;

        DashboardTask(String email, String date, String e_id) {
            this.date = date;
            this.e_id = e_id;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            dashboard = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Dashboard> call = api.GetUserDashAdmin(email, date, e_id);
            call.enqueue(new Callback<Dashboard>() {
                @Override
                public void onResponse(@NonNull Call<Dashboard> call, @NonNull Response<Dashboard> response) {
                    if (response.body() != null) {
                        dashboard = response.body();
                        i = dashboard.code;
                    } else {
                        dashboard = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Dashboard> call, @NonNull Throwable t) {
                    dashboard = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    dashboard = null;
                    break;
                }
                if (i != 4869)
                    return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            if (dashboard != null) {
                Toast.makeText(AppController.getInstance(), dashboard.message, Toast.LENGTH_SHORT).show();
                if (dashboard.code == 1) {
                    UserDash.this.dashboard = dashboard;
                    bottomNavigationView.setSelectedItemId(R.id.dashboard);
                }
            } else if (i == 306)
                Toast.makeText(AppController.getInstance(), "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(AppController.getInstance(), "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(AppController.getInstance(), "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}