package io.github.sher1234.service.ui.v2.b.fragment.graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.v4.GraphAdapter;
import io.github.sher1234.service.adapter.v4.OnItemClick;

public class Graph extends Fragment implements RadioGroup.OnCheckedChangeListener,
        OnItemClick<Map<String, Integer>> {

    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;

    public Graph() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        View view = inflater.inflate(R.layout.b_fragment_1, group, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        radioGroup = view.findViewById(R.id.radioGroup);
        textView = view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        radioGroup.setOnCheckedChangeListener(this);
        //test code
        test();
    }

    private void onResetGraph(List<Integer> requests, List<Integer> visits) {
        GraphAdapter graphAdapter = (GraphAdapter) recyclerView.getAdapter();
        if (graphAdapter != null) graphAdapter.onGraphUpdate(requests, visits);
        else {
            graphAdapter = GraphAdapter.newInstance(this, requests, visits);
            recyclerView.setAdapter(graphAdapter);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup chipGroup, int i) {
        switch (i) {
            case R.id.radio1: break;

            case R.id.radio2: break;
        }
    }

    @Override
    public void onClick(Map<String, Integer> map) {
        String s = "Requests: " + map.get("request") + "\n" + "Visits: " + map.get("visit");
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
    //Test Code
    private List<Integer> getTestIntegers() {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            integers.add(new Random().nextInt(10));
        return integers;
    }

    private void test() {
        onResetGraph(getTestIntegers(), getTestIntegers());
    }
}