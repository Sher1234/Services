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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.DashboardPager;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.NavigationIconClickListener;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {


    private boolean exit = false;
    private ViewPager viewPager;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private View mProgressView;
    private DashboardTask task;
    private User user;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(this,
                findViewById(R.id.coordinatorLayout),
                new AccelerateDecelerateInterpolator(),
                getResources().getDrawable(R.drawable.ic_menu),
                getResources().getDrawable(R.drawable.ic_close)));

        findViewById(R.id.button1).setSelected(true);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);

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
        user = getUserFromPreferences();
        Calendar calendar = Calendar.getInstance();
        date = new SimpleDateFormat(Strings.DateFormatServer).format(calendar.getTime());
        textView2.setText(user.getEmail());
        textView1.setText(user.getName());
        textView3.setText("0");
        if (task != null)
            task.cancel(true);
        task = new DashboardTask(user.getEmail(), date);
        task.execute();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh: {
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new DashboardTask(user.getEmail(), date);
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

        private Dashboard dashboard;

        PagerAdapter(FragmentManager fm, Dashboard dashboard) {
            super(fm);
            this.dashboard = dashboard;
        }

        @Override
        public Fragment getItem(int i) {
            return DashboardPager.getInstance(i, dashboard);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DashboardTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String dateTime;

        private Dashboard dashboard;
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
            Call<Dashboard> call = api.GetDashboard(email, dateTime);
            call.enqueue(new Callback<Dashboard>() {
                @Override
                public void onResponse(@NonNull Call<Dashboard> call, @NonNull Response<Dashboard> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        dashboard = response.body();
                        i = dashboard.getResponse().getCode();
                    } else {
                        dashboard = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Dashboard> call, @NonNull Throwable t) {
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