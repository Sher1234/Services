package io.github.sher1234.service.ui.i;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.VisitAdapter;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskShow;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.Visits;
import io.github.sher1234.service.model.response.Services;
import io.github.sher1234.service.ui.i.fragment.ShowCall;
import io.github.sher1234.service.ui.i.fragment.ShowVisit;
import io.github.sher1234.service.ui.i.fragment.ShowVisits;
import io.github.sher1234.service.util.Strings;

public class Show extends AppCompatActivity implements TaskShow.TaskUpdate, VisitAdapter.ItemClick {

    private final Common common = new Common();
    private AppCompatTextView textViewTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TaskShow tasS;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_activity);
        string = getIntent().getStringExtra(Strings.ExtraString);
        tasS = new TaskShow();
        if (string == null || string.isEmpty()) {
            Toast.makeText(this, "Invalid Call ID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(null);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        textViewTitle = findViewById(R.id.textViewTitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        fetch();
    }

    public void onAttachVisit(boolean b) {
        if (b) {
            textViewTitle.setText(R.string.visit_details);
            tabLayout.setVisibility(View.GONE);
        }
    }

    public void onDetachVisit() {
        textViewTitle.setText(R.string.call_details);
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void onReloadCall() {
        Intent intent = new Intent(this, Show.class);
        intent.putExtra(Strings.ExtraString, string);
        startActivity(intent);
        finish();
    }

    private void onChangeColor(boolean b) {
        if (b) {
            tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_item_green));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.green));
            textViewTitle.setTextColor(getResources().getColor(R.color.green));
        } else {
            tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_item_red));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));
            textViewTitle.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void onCallRefreshed(Services services) {
        if (viewPager.getAdapter() == null)
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), services));
        else {
            PagerAdapter pagerAdapter = (PagerAdapter) viewPager.getAdapter();
            pagerAdapter.onCallUpdate(services);
        }
        onChangeColor(services.call.isCompleted());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment == null) super.onBackPressed();
        else getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onFetched(Services services, int i) {
        common.dismissProgressDialog();
        if (services != null) {
            Snackbar.make(textViewTitle, services.message, Snackbar.LENGTH_SHORT).show();
            if (services.call != null) onCallRefreshed(services);
            else Snackbar.make(textViewTitle, "Invalid Call", Snackbar.LENGTH_INDEFINITE).show();
        } else {
            if (i == 306)
                Snackbar.make(textViewTitle, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(textViewTitle, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(textViewTitle, "Request cancelled", Snackbar.LENGTH_LONG).show();
            tasS.onNetworkError(this, this);
        }
    }

    @Override
    public void onFetch() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (f != null) getSupportFragmentManager().beginTransaction().remove(f).commit();
        common.showProgressDialog(this);
    }

    @Override
    public void fetch() {
        if (string != null && !string.isEmpty()) tasS.onRefreshCall(this, string);
        else Snackbar.make(textViewTitle, "Invalid Call ID", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Call call, Visits visit, View v) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, ShowVisit.getInstance(call, visit, true))
                .commit();
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private Services services;

        PagerAdapter(FragmentManager fragmentManager, @NotNull Services services) {
            super(fragmentManager);
            this.services = services;
        }

        private void onCallUpdate(Services services) {
            this.services = services;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    if (services.visits == null || services.visits.size() != 1)
                        return ShowVisits.getInstance(services);
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    assert tab != null;
                    tab.setText(R.string.visit);
                    return ShowVisit.getInstance(services.call, services.visits.get(0), false);

                default:
                    return ShowCall.getInstance(services);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}