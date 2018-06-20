package io.github.sher1234.service.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.fragment.Call1;
import io.github.sher1234.service.fragment.Call2;
import io.github.sher1234.service.fragment.Call3;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.NavigationHost;
import io.github.sher1234.service.util.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewActivity extends AppCompatActivity implements NavigationHost {

    private LinearLayout linearLayout;
    private ViewPager viewPager;
    private ImageView imageView;
    private TabLayout tabLayout;
    private TextView textView1;
    private TextView textView2;
    private View mProgressView;
    private Toolbar toolbar;
    private CallTask task;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        string = getIntent().getStringExtra(Strings.ExtraString);
        if (string == null || string.isEmpty()) {
            Toast.makeText(this, "Invalid Call, Exiting...", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 550);
            return;
        }
        mProgressView = findViewById(R.id.progressView);
        linearLayout = findViewById(R.id.linearLayout);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        imageView = findViewById(R.id.imageView);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(color));
        linearLayout.setBackgroundResource(color);
        tabLayout.setBackgroundResource(color);
        toolbar.setBackgroundResource(color);
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView1.setText(string);
        textView2.setText("");
        imageView.setImageResource(R.drawable.ic_pending_2);
        setStatusBarColor(R.color.colorPrimaryDark);
        if (task != null)
            task.cancel(true);
        task = null;
        task = new CallTask(string);
        task.execute();
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

    @Override
    public void navigateTo(@NotNull Fragment fragment, boolean isBackStacked) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right,
                R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                .add(R.id.frameLayout, fragment, Strings.TAG).commit();
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private ServiceCall serviceCall;

        PagerAdapter(FragmentManager fragmentManager, @NotNull ServiceCall serviceCall) {
            super(fragmentManager);
            this.serviceCall = serviceCall;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    if (serviceCall.getVisits() == null || serviceCall.getVisits().size() != 1)
                        return Call2.getInstance(serviceCall);
                    tabLayout.getTabAt(1).setText("Visit");
                    return Call3.getInstance(serviceCall, 0);

                default:
                    return Call1.getInstance(serviceCall);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class CallTask extends AsyncTask<Void, Void, Boolean> {

        private String string;

        private ServiceCall serviceCall;
        private int i = 4869;

        CallTask(String string) {
            this.string = string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            serviceCall = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<ServiceCall> call = api.GetServiceCall(string);
            call.enqueue(new Callback<ServiceCall>() {
                @Override
                public void onResponse(@NonNull Call<ServiceCall> call, @NonNull Response<ServiceCall> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        serviceCall = response.body();
                        i = serviceCall.getResponse().getCode();
                    } else {
                        serviceCall = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ServiceCall> call, @NonNull Throwable t) {
                    serviceCall = null;
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
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
            showProgress(false);
            if (serviceCall != null) {
                Toast.makeText(AppController.getInstance(), serviceCall.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                if (serviceCall.getResponse().getCode() == 1) {
                    PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), serviceCall);
                    textView2.setText(serviceCall.getRegistration().getCustomerName());
                    if (serviceCall.getRegistration().isCompleted()) {
                        setStatusBarColor(R.color.colorCompleted);
                        imageView.setImageResource(R.drawable.ic_completed);
                    } else {
                        setStatusBarColor(R.color.colorPending);
                        imageView.setImageResource(R.drawable.ic_pending);
                    }
                    viewPager.setAdapter(pagerAdapter);
                }
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}