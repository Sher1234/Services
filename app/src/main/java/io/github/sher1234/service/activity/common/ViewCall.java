package io.github.sher1234.service.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.ViewReg;
import io.github.sher1234.service.fragment.ViewVisit;
import io.github.sher1234.service.fragment.ViewVisits;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressWarnings("all")
public class ViewCall extends AppCompatActivity {

    private final Functions functions = new Functions();
    private CoordinatorLayout coordinatorLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View progressView;
    private CallTask task;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);
        string = getIntent().getStringExtra(Strings.ExtraString);
        if (string == null || string.isEmpty()) {
            Toast.makeText(this, "Invalid call, exiting...", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);
            return;
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        coordinatorLayout = findViewById(R.id.toolbar);
        progressView = findViewById(R.id.progressView);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        onStartTask();
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(color));
            getWindow().setNavigationBarColor(getResources().getColor(color));
        }
        tabLayout.setBackgroundResource(color);
        coordinatorLayout.setBackgroundResource(color);
    }

    private void onStartTask() {
        setStatusBarColor(R.color.gPurpleDark);
        if (task != null)
            task.cancel(true);
        task = null;
        task = new CallTask(string);
        task.execute();
    }

    public void onTapRefresh() {
        setStatusBarColor(R.color.gPurpleDark);
        functions.showProgress(true, progressView);
        Intent intent = new Intent(this, ViewCall.class);
        intent.putExtra(Strings.ExtraString, string);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(Strings.TAG);
        if (fragment != null)
            manager.beginTransaction().setCustomAnimations(R.anim.enter_right,
                    R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                    .remove(fragment).commit();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    public void navigateToFragment(@NotNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,
                R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                .add(R.id.frameLayout, fragment, Strings.TAG).commit();
    }

    private void onDataUpdate(ServiceCall call) {
        if (viewPager.getAdapter() == null)
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), call));
        else {
            PagerAdapter pagerAdapter = (PagerAdapter) viewPager.getAdapter();
            pagerAdapter.onCallUpdate(call);
        }
        if (call.registration.isCompleted()) {
            setStatusBarColor(R.color.gGreen);
        } else {
            setStatusBarColor(R.color.gRed);
        }
    }

    public void onDelete(String s, boolean b) {
        new DeleteTask(s, b).execute();
    }

    public void navigateWithData(@NotNull Class c, @NotNull Serializable serializable, int reqCode) {
        Intent intent = new Intent(this, c);
        intent.putExtra(Strings.ExtraData, serializable);
        startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4869) {
            if (resultCode == 404)
                Toast.makeText(this, "Edit error...", Toast.LENGTH_SHORT).show();
            if (resultCode == 101)
                Toast.makeText(this, "No changes made...", Toast.LENGTH_SHORT).show();
            if (resultCode == 200) {
                Toast.makeText(this, "Refreshing call...", Toast.LENGTH_SHORT).show();
                onTapRefresh();
            }
        }
        if (requestCode == 9684) {
            if (resultCode == 404)
                Toast.makeText(this, "Sign error...", Toast.LENGTH_SHORT).show();
            if (resultCode == 101)
                Toast.makeText(this, "No sign uploaded...", Toast.LENGTH_SHORT).show();
            if (resultCode == 200) {
                Toast.makeText(this, "Refreshing call...", Toast.LENGTH_SHORT).show();
                onTapRefresh();
            }
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private ServiceCall serviceCall;

        PagerAdapter(FragmentManager fragmentManager, @NotNull ServiceCall serviceCall) {
            super(fragmentManager);
            this.serviceCall = serviceCall;
        }

        private void onCallUpdate(ServiceCall serviceCall) {
            this.serviceCall = serviceCall;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    if (serviceCall.visits == null || serviceCall.visits.size() != 1)
                        return ViewVisits.getInstance(serviceCall);
                    tabLayout.getTabAt(1).setText(R.string.visit);
                    return ViewVisit.getInstance(serviceCall.registration, serviceCall.visits.get(0));

                default:
                    return ViewReg.getInstance(serviceCall);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class CallTask extends AsyncTask<Void, Void, Boolean> {

        private final String string;

        private ServiceCall serviceCall;
        private int i = 4869;

        CallTask(String string) {
            this.string = string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, progressView);
            serviceCall = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<ServiceCall> call = api.GetCall(string);
            call.enqueue(new Callback<ServiceCall>() {
                @Override
                public void onResponse(@NonNull Call<ServiceCall> call, @NonNull Response<ServiceCall> response) {
                    if (response.body() != null) {
                        serviceCall = response.body();
                        i = serviceCall.code;
                    } else {
                        serviceCall = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ServiceCall> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    serviceCall = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    serviceCall = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            functions.showProgress(false, progressView);
            if (serviceCall != null) {
                Toast.makeText(AppController.getInstance(), serviceCall.message, Toast.LENGTH_SHORT).show();
                if (serviceCall.code == 1)
                    onDataUpdate(serviceCall);
            } else if (i == 306)
                Toast.makeText(ViewCall.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(ViewCall.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(ViewCall.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DeleteTask extends AsyncTask<Void, Void, Boolean> {

        private final String string;
        private final boolean b;

        private Responded responded;
        private int i = 4869;

        DeleteTask(String string, boolean b) {
            this.string = string;
            this.b = b;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, progressView);
            responded = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call;
            if (b) call = api.DeleteCall(string);
            else call = api.DeleteVisit(string);
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Responded> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    responded = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    responded = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            functions.showProgress(false, progressView);
            if (responded != null) {
                Toast.makeText(AppController.getInstance(), responded.Message, Toast.LENGTH_SHORT).show();
                if (responded.Code == 1)
                    finish();
            } else if (i == 306)
                Toast.makeText(ViewCall.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(ViewCall.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(ViewCall.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}