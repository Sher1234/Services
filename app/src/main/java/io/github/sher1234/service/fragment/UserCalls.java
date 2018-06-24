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
import android.widget.Toast;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.RegisterRecycler;
import io.github.sher1234.service.model.response.DashboardWR;

public class UserCalls extends Fragment {

    private static final String TAG_ARGS = "SERIALIZABLE-PAGER-MAIN";

    private DashboardWR dashboard;

    private RecyclerView recyclerView;

    public UserCalls() {
    }

    public static UserCalls getInstance(DashboardWR dashboard) {
        UserCalls dashboardPager = new UserCalls();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_ARGS, dashboard);
        dashboardPager.setArguments(bundle);
        return dashboardPager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        dashboard = (DashboardWR) getArguments().getSerializable(TAG_ARGS);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getContext(), "Loading Calls...", Toast.LENGTH_SHORT).show();
        RegisterRecycler registerRecycler = new RegisterRecycler(getContext(), dashboard.getRegistrations());
        recyclerView.setAdapter(registerRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}