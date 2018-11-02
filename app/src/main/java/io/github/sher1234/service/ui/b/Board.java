package io.github.sher1234.service.ui.b;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.Navigate;
import io.github.sher1234.service.functions.TaskBoard;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.ui.b.fragment.Calls;
import io.github.sher1234.service.ui.b.fragment.Pager;
import io.github.sher1234.service.ui.d.AddCall;

public class Board extends AppCompatActivity implements TaskBoard.TaskUpdate, View.OnClickListener {

    private final TaskBoard taskBoard = new TaskBoard();
    private final User user = new TaskUser().getUser();
    private final Common common = new Common();
    private AppCompatTextView textView1, textView2, textView3;
    private AppCompatTextView textViewTitle;
    private AppBarLayout appBarLayout;
    private LinearLayoutCompat layout;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private boolean exit = false;
    private Dashboard dashboard;
    private Navigate navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (user == null) {
            Toast.makeText(this, "Invalid User", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.b_activity);
        navigate = new Navigate(this, 1);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        textViewTitle = findViewById(R.id.textViewTitle);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        onResetInfoCard();
        onResetViewPager();
        onFragmentDetached();
        fetch();
    }

    public void onFragmentAttached() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.back);
        textViewTitle.setText(R.string.pending_calls);
        appBarLayout.setLiftOnScroll(false);
        tabLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        navigate.onLockDrawer();
        fab.hide();
    }

    public void onFragmentDetached() {
        toolbar.setNavigationIcon(R.drawable.menu);
        textViewTitle.setText(R.string.dashboard);
        tabLayout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        appBarLayout.setLiftOnScroll(true);
        navigate.onUnlockDrawer();
        navigate.onResetToolbar();
        fab.show();
    }

    private void onResetViewPager() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setAdapter(null);
    }

    private void onResetInfoCard() {
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        layout = findViewById(R.id.linearLayout);
        textView1.setText(getInfoString("Calls", "NULL"));
        textView2.setText(getInfoString("Visits", "NULL"));
        textView3.setText(getInfoString("Pending", "NULL"));
        layout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigate.onResumeActivity();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (exit) {
            super.onBackPressed();
            return;
        }
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        else {
            exit = true;
            Snackbar.make(textViewTitle, "Press back again to exit", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }

    private String getInfoString(String type, String value) {
        return type + ": " + value;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.linearLayout)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, Calls.getInstance(dashboard))
                    .commit();
        if (view.getId() == R.id.fab)
            startActivity(new Intent(this, AddCall.class));
    }

    private void onRefresh(Dashboard dashboard) {
        PagerAdapter pagerAdapter = (PagerAdapter) viewPager.getAdapter();
        if (pagerAdapter != null) pagerAdapter.setDashboard(dashboard);
        else {
            pagerAdapter = new PagerAdapter(getSupportFragmentManager(), dashboard);
            viewPager.setAdapter(pagerAdapter);
        }
        this.dashboard = dashboard;
        textView3.setText(getInfoString("Pending", dashboard.pCalls));
        textView2.setText(getInfoString("Visits", dashboard.tVisits));
        textView1.setText(getInfoString("Calls", dashboard.tCalls));
    }

    @Override
    public void onFetched(@Nullable Dashboard dashboard, int i) {
        common.dismissProgressDialog();
        if (dashboard != null) {
            Snackbar.make(textViewTitle, dashboard.message, Snackbar.LENGTH_SHORT).show();
            onRefresh(dashboard);
        } else {
            if (i == 306)
                Snackbar.make(textViewTitle, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(textViewTitle, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(textViewTitle, "Request cancelled", Snackbar.LENGTH_LONG).show();
            taskBoard.onNetworkError(this, this);
        }
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
        onResetInfoCard();
    }

    @Override
    public void fetch() {
        taskBoard.onRefreshDashboard(this, this);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private Dashboard dashboard;

        PagerAdapter(FragmentManager fragmentManager, Dashboard dashboard) {
            super(fragmentManager);
            this.dashboard = dashboard;
        }

        void setDashboard(Dashboard dashboard) {
            this.dashboard = dashboard;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            return Pager.getInstance(dashboard, i);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}