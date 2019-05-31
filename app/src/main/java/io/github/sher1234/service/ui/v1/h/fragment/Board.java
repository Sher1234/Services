package io.github.sher1234.service.ui.v1.h.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.ui.v1.b.fragment.Pager;
import io.github.sher1234.service.ui.v1.h.EmployeeBoard;
import io.github.sher1234.service.util.Strings;

public class Board extends Fragment {

    @Nullable
    private Dashboard dashboard;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public Board() {
    }

    public static Board getInstance(@Nullable Dashboard dashboard) {
        Board pager = new Board();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, dashboard);
        pager.setArguments(bundle);
        return pager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        dashboard = (Dashboard) getArguments().getSerializable(Strings.ExtraData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null)
            ((EmployeeBoard) getActivity()).onFragmentAttached(true, R.string.dashboard);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null)
            ((EmployeeBoard) getActivity()).onFragmentDetached();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_fragment_2, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager.setAdapter(null);
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
            return Pager.getInstance(dashboard, i);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}