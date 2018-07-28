package io.github.sher1234.service.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.admin.UserPrivileges;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.DashPending;
import io.github.sher1234.service.fragment.DashboardPager;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.BottomMenuListener;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.NavigateActivity;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashBoard extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener, NavigateActivity, View.OnClickListener {


    private final Functions functions = new Functions();
    private Dashboard dashboard = null;
    private boolean exit = false;
    private ViewPager viewPager;
    private DashboardTask task;
    private View mProgressView;
    private TextView textView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        user = AppController.getUserFromPrefs();

        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(new BottomMenuListener(getSupportFragmentManager(), 1));
        bottomAppBar.setOnMenuItemClickListener(this);
        if (user.isAdmin())
            bottomAppBar.replaceMenu(R.menu.admin_dash);
        else
            bottomAppBar.replaceMenu(R.menu.user_dash);

        textView = findViewById(R.id.textView);
        viewPager = findViewById(R.id.viewPager);
        mProgressView = findViewById(R.id.progressView);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        findViewById(R.id.floatingActionButton).setOnClickListener(this);
        textView.setOnClickListener(this);
        onStartTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    private void onStartTask() {
        String s = getResources().getString(R.string.pending_calls) + ": 0";
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setText(Html.fromHtml(s));
        Calendar calendar = Calendar.getInstance();
        String date = new SimpleDateFormat(Strings.DateServer, Locale.US).format(calendar.getTime());
        if (task != null)
            task.cancel(true);
        task = new DashboardTask(date);
        task.execute();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Strings.TAG + "D-P");
        if (exit) {
            super.onBackPressed();
            return;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            functions.showProgress(false, findViewById(R.id.cardView));
        } else {
            exit = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 1500);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.users) {
            navigateToActivity(UserPrivileges.class, false);
            return true;
        } else if (menuItem.getItemId() == R.id.refresh) {
            onStartTask();
            return true;
        }
        return false;
    }

    @Override
    public void navigateToActivity(@NotNull Class c, boolean finish) {
        startActivity(new Intent(this, c));
        if (finish)
            finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.textView) {
            functions.showProgress(true, findViewById(R.id.cardView));
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.cardView, DashPending.getInstance(dashboard),
                            Strings.TAG + "D-P")
                    .commit();
        } else if (view.getId() == R.id.floatingActionButton)
            navigateToActivity(RegisterCall.class, false);
    }

    private void onRefresh(Dashboard dashboard) {
        PagerAdapter pagerAdapter = (PagerAdapter) viewPager.getAdapter();
        if (pagerAdapter != null) {
            pagerAdapter.setDashboard(dashboard);
        } else {
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(), dashboard);
            viewPager.setAdapter(pagerAdapter);
        }
        String s = getResources().getString(R.string.pending_calls) + ": " + dashboard.pending;
        this.dashboard = dashboard;
        textView.setText(s);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private Dashboard dashboard;

        PagerAdapter(FragmentManager fm, Dashboard dashboard) {
            super(fm);
            this.dashboard = dashboard;
        }

        void setDashboard(Dashboard dashboard) {
            this.dashboard = dashboard;
            notifyDataSetChanged();
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

        private final String dateTime;
        private Dashboard dashboard;
        private int i = 4869;

        DashboardTask(String dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, mProgressView);
            dashboard = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Dashboard> call;
            if (user.isAdmin())
                call = api.GetAdminDash(dateTime);
            else
                call = api.GetUserDash(user.Email, dateTime, user.EmployeeID);
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
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            functions.showProgress(false, mProgressView);
            if (dashboard != null) {
                Toast.makeText(AppController.getInstance(), dashboard.message, Toast.LENGTH_SHORT).show();
                if (dashboard.code == 1)
                    onRefresh(dashboard);
            } else if (i == 306)
                Toast.makeText(DashBoard.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(DashBoard.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(DashBoard.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}