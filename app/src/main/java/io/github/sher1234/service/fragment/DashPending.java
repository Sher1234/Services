package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.Calls;
import io.github.sher1234.service.adapter.CallRecycler;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.NavigateActivity;
import io.github.sher1234.service.util.Strings;

public class DashPending extends Fragment implements View.OnClickListener {

    private View layout;
    private Dashboard dashboard;
    private RecyclerView recyclerView;

    public DashPending() {
    }

    public static DashPending getInstance(Dashboard dashboard) {
        Bundle bundle = new Bundle();
        DashPending dashboardPager = new DashPending();
        bundle.putSerializable(Strings.ExtraData, dashboard);
        dashboardPager.setArguments(bundle);
        return dashboardPager;
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
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        layout = view.findViewById(R.id.coordinatorLayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (dashboard != null && dashboard.pRegs != null)
            recyclerView.setAdapter(new CallRecycler(getContext(), dashboard.pRegs));
        else
            layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1 || view.getId() == R.id.button2) {
            assert getActivity() != null;
            ((NavigateActivity) getActivity()).navigateToActivity(Calls.class, true);
        }
    }
}