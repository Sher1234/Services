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
import android.widget.TextView;
import android.widget.Toast;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.CallRecycler;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.Strings;

public class CallsFrag extends Fragment {

    private TextView textView;
    private Dashboard dashboard;
    private RecyclerView recyclerView;

    public CallsFrag() {
    }

    public static CallsFrag newInstance(Dashboard dashboard) {
        CallsFrag frag = new CallsFrag();
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
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (dashboard == null)
            Toast.makeText(getContext(), "Invalid data...", Toast.LENGTH_SHORT).show();
        else {
            if (dashboard.pRegs == null || dashboard.pRegs.isEmpty()) {
                textView.setVisibility(View.VISIBLE);
                return;
            }
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            CallRecycler callRecycler = new CallRecycler(getContext(), dashboard.pRegs);
            recyclerView.setAdapter(callRecycler);
        }
    }
}