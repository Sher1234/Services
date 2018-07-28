package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.ProgressRecycler;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.Strings;

public class DashboardPager extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private int MODE;
    private Dashboard dashboard;
    private RadioGroup radioGroup;
    private AppCompatTextView textView4, textView5;
    private TextView textView1, textView2, textView3;

    private RecyclerView recyclerView;

    public DashboardPager() {
    }

    public static DashboardPager getInstance(int i, Dashboard dashboard) {
        DashboardPager dashboardPager = new DashboardPager();
        Bundle bundle = new Bundle();
        bundle.putInt(Strings.ExtraString, i);
        bundle.putSerializable(Strings.ExtraData, dashboard);
        dashboardPager.setArguments(bundle);
        return dashboardPager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        MODE = getArguments().getInt(Strings.ExtraString, 0);
        dashboard = (Dashboard) getArguments().getSerializable(Strings.ExtraData);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        radioGroup = view.findViewById(R.id.radioGroup);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView5 = view.findViewById(R.id.textView5);
        textView4 = view.findViewById(R.id.textView4);
        textView3 = view.findViewById(R.id.textView3);
        textView2 = view.findViewById(R.id.textView2);
        textView1 = view.findViewById(R.id.textView1);
        radioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        radioGroup.check(R.id.radioButton1);
    }

    private void setFields(int i) {
        if (MODE == 0) {
            if (i == 0)
                onUpdateView(dashboard.rDaily.getDailyList(), dashboard.dDates);
            else
                onUpdateView(dashboard.rMonthly.getMonthlyList(), dashboard.mDates);
        } else {
            if (i == 0)
                onUpdateView(dashboard.vDaily.getDailyList(), dashboard.dDates);
            else
                onUpdateView(dashboard.vMonthly.getMonthlyList(), dashboard.mDates);
        }
    }

    private void onUpdateView(List<Integer> integers, List<String> strings) {
        ProgressRecycler progressRecycler = (ProgressRecycler) recyclerView.getAdapter();
        if (progressRecycler != null) {
            progressRecycler.setIntegers(integers);
            progressRecycler.setStrings(strings);
            progressRecycler.notifyDataSetChanged();
        } else {
            progressRecycler = new ProgressRecycler(getContext(), integers, strings);
            recyclerView.setAdapter(progressRecycler);
        }
        String s = strings.get(0) + " - " + strings.get(strings.size() - 1);
        textView1.setText(String.valueOf(progressRecycler.max));
        textView3.setText(String.valueOf(0));
        int m = progressRecycler.max / 2;
        textView2.setText(String.valueOf(m));
        textView5.setText(s);
    }

    @Override
    public void onCheckedChanged(RadioGroup chipGroup, int i) {
        String s;
        switch (i) {
            case R.id.radioButton1:
                setFields(0);
                if (MODE == 0)
                    s = "Daily " + getResources().getString(R.string.registrations);
                else
                    s = "Daily " + getResources().getString(R.string.visits);
                textView4.setText(s);
                break;

            case R.id.radioButton2:
                setFields(1);
                if (MODE == 0)
                    s = "Monthly " + getResources().getString(R.string.registrations);
                else
                    s = "Monthly " + getResources().getString(R.string.visits);
                textView4.setText(s);
                break;
        }
    }
}