package io.github.sher1234.service.ui.b.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.GraphAdapter;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.util.Strings;

public class Pager extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private int MODE;
    private Dashboard dashboard;
    private RadioGroup radioGroup;
    private AppCompatTextView textView;

    private RecyclerView recyclerView;

    public Pager() {
    }

    public static Pager getInstance(@Nullable Dashboard dashboard, int i) {
        Pager pager = new Pager();
        Bundle bundle = new Bundle();
        bundle.putInt(Strings.ExtraString, i);
        bundle.putSerializable(Strings.ExtraData, dashboard);
        pager.setArguments(bundle);
        return pager;
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
        View view = inflater.inflate(R.layout.b_fragment_1, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        radioGroup = view.findViewById(R.id.radioGroup);
        textView = view.findViewById(R.id.textView);
        radioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Calendar.getInstance().get(Calendar.MONTH) > 6)
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    RecyclerView.HORIZONTAL, false));
        else recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        radioGroup.clearCheck();
        radioGroup.check(R.id.button1);
    }

    private void setFields(int i) {
        if (MODE == 0)
            if (i == 0) onUpdateView(dashboard.dCalls, dashboard.days);
            else onUpdateView(dashboard.mCalls, dashboard.months);
        else if (i == 0) onUpdateView(dashboard.dVisits, dashboard.days);
        else onUpdateView(dashboard.mVisits, dashboard.months);
    }

    private void onUpdateView(List<Integer> integers, List<String> strings) {
        GraphAdapter graphAdapter = (GraphAdapter) recyclerView.getAdapter();
        if (graphAdapter != null) graphAdapter.onGraphUpdate(integers, strings);
        else {
            graphAdapter = new GraphAdapter(getContext(), integers, strings);
            recyclerView.setAdapter(graphAdapter);
        }
        textView.setText(strings.get(0).contains("-#-") ? strings.get(0).split("-#-")[1] + " to " + strings.get(strings.size() - 1).split("-#-")[1] : strings.get(0) + " to " + strings.get(strings.size() - 1));
    }

    @Override
    public void onCheckedChanged(RadioGroup chipGroup, int i) {
        switch (i) {
            case R.id.button1:
                setFields(0);
                break;

            case R.id.button2:
                setFields(1);
                break;
        }
    }
}