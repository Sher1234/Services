package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.NavigationHost;

public class Main1 extends Fragment implements View.OnClickListener {

    public Main1() {
    }

    private BarChart barChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_1, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        barChart = view.findViewById(R.id.barChart);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<BarEntry> barEntries;
        ArrayList<String> strings;
        barEntries = addValuesToBarEntries();
        strings = addValuesToStrings();
        BarDataSet barDataSet = new BarDataSet(barEntries, "Projects");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setBarSpacePercent(50);
        barChart.setTouchEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        BarData barData = new BarData(strings, barDataSet);
        barChart.setData(barData);
        barChart.animateY(3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                assert getActivity() != null;
                ((NavigationHost) getActivity()).navigateTo(new Start2(), true);
                break;

            case R.id.button2:
                assert getActivity() != null;
                ((NavigationHost) getActivity()).navigateTo(new Start3(), true);
                break;
        }
    }

    public ArrayList<BarEntry> addValuesToBarEntries() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2f, 0));
        barEntries.add(new BarEntry(4f, 1));
        barEntries.add(new BarEntry(6f, 2));
        barEntries.add(new BarEntry(8f, 3));
        return barEntries;

    }

    public ArrayList<String> addValuesToStrings() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("January");
        strings.add("February");
        strings.add("March");
        strings.add("April");
        return strings;
    }
}
