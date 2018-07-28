package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.Strings;

public class DashFrag extends Fragment {

    private Dashboard dashboard;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public DashFrag() {
    }

    public static DashFrag newInstance(Dashboard dashboard) {
        DashFrag frag = new DashFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, dashboard);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        dashboard = (Dashboard) getArguments().getSerializable(Strings.ExtraData);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), dashboard));
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private final Dashboard dashboard;

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
}