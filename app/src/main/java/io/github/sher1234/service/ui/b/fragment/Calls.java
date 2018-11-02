package io.github.sher1234.service.ui.b.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.CallsAdapter;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.ui.b.Board;
import io.github.sher1234.service.ui.i.Show;
import io.github.sher1234.service.util.Strings;

public class Calls extends Fragment implements CallsAdapter.ItemClick {

    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private Dashboard dashboard;

    public Calls() {
    }

    public static Calls getInstance(Dashboard dashboard) {
        Bundle bundle = new Bundle();
        Calls dashboardPager = new Calls();
        bundle.putSerializable(Strings.ExtraData, dashboard);
        dashboardPager.setArguments(bundle);
        return dashboardPager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null)
            ((Board) getActivity()).onFragmentAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null)
            ((Board) getActivity()).onFragmentDetached();
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
        View view = inflater.inflate(R.layout.b_fragment_2, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (dashboard != null && dashboard.calls != null && dashboard.calls.size() > 0)
            recyclerView.setAdapter(new CallsAdapter(this, dashboard.calls));
        else
            textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Call call, View v) {
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra(Strings.ExtraString, call.CallID);
        startActivity(intent);
    }
}