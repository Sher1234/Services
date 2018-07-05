package io.github.sher1234.service.activity.admin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.AccountActivity;
import io.github.sher1234.service.activity.CallsActivity;
import io.github.sher1234.service.activity.RegCallActivity;
import io.github.sher1234.service.activity.StartActivity;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.DashboardPager;
import io.github.sher1234.service.fragment.UserCalls;
import io.github.sher1234.service.model.response.DashboardWR;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {


    private ViewPager viewPager;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private View mProgressView;
    private DashboardTask task;
    private String email;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_db);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        mProgressView = findViewById(R.id.progressView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        viewPager = findViewById(R.id.viewPager);
        textView3.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onStart() {
        super.onStart();
        Calendar calendar = Calendar.getInstance();
        date = new SimpleDateFormat(Strings.DateFormatServer).format(calendar.getTime());
        String name = getIntent().getStringExtra("Intent:Name");
        email = getIntent().getStringExtra("Intent:Email");
        textView1.setText(name);
        textView2.setText(email);
        textView3.setText("0");
        if (task != null)
            task.cancel(true);
        task = new DashboardTask(email, date);
        task.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                break;

            case R.id.button2:
                startActivity(new Intent(this, CallsActivity.class));
                finish();
                break;

            case R.id.button3:
                startActivity(new Intent(this, RegCallActivity.class));
                break;

            case R.id.button4:
                startActivity(new Intent(this, AccountActivity.class));
                finish();
                break;

            case R.id.button5:
                break;

            case R.id.button6:
                onLogoutUser();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh: {
                Calendar calendar = Calendar.getInstance();
                date = new SimpleDateFormat(Strings.DateFormatServer).format(calendar.getTime());
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new DashboardTask(email, date);
                task.execute();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
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
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final DashboardWR dashboard;

        PagerAdapter(FragmentManager fm, DashboardWR dashboard) {
            super(fm);
            this.dashboard = dashboard;
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 2)
                return UserCalls.getInstance(dashboard);
            else
                return DashboardPager.getInstance(i, dashboard);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DashboardTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String dateTime;

        private DashboardWR dashboard;
        private int i = 4869;

        DashboardTask(String email, String dateTime) {
            this.email = email;
            this.dateTime = dateTime;
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
            Call<DashboardWR> call = api.GetDashboardWR(email, dateTime);
            call.enqueue(new Callback<DashboardWR>() {
                @Override
                public void onResponse(@NonNull Call<DashboardWR> call, @NonNull Response<DashboardWR> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        dashboard = response.body();
                        i = dashboard.getResponse().getCode();
                    } else {
                        dashboard = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DashboardWR> call, @NonNull Throwable t) {
                    dashboard = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    dashboard = null;
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
            if (dashboard != null) {
                Toast.makeText(AppController.getInstance(), dashboard.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                if (dashboard.getResponse().getCode() == 1) {
                    PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), dashboard);
                    textView3.setText(dashboard.getPendingAll());
                    viewPager.setAdapter(pagerAdapter);
                }
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}