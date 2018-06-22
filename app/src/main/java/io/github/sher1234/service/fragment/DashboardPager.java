package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.response.Dashboard;

public class DashboardPager extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG_ARGS_1 = "INTEGER-PAGER-MAIN";
    private static final String TAG_ARGS_2 = "SERIALIZABLE-PAGER-MAIN";

    private int MODE;
    private BarData barDataW;
    private BarData barDataQ;
    private String description;
    private Dashboard dashboard;

    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private BarChart barChart;

    public DashboardPager() {
    }

    public static DashboardPager getInstance(int i, Dashboard dashboard) {
        DashboardPager dashboardPager = new DashboardPager();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_ARGS_1, i);
        bundle.putSerializable(TAG_ARGS_2, dashboard);
        dashboardPager.setArguments(bundle);
        return dashboardPager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        MODE = getArguments().getInt(TAG_ARGS_1, 0);
        dashboard = (Dashboard) getArguments().getSerializable(TAG_ARGS_2);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton2 = view.findViewById(R.id.radioButton2);
        barChart = view.findViewById(R.id.barChart);
        radioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setFields();
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setPinchZoom(false);
        updateBarData(barDataW, "Weekly");
    }

    private void setFields() {
        List<BarEntry> barEntriesW;
        List<BarEntry> barEntriesQ;
        List<String> barLabelsW;
        List<String> barLabelsQ;
        if (MODE == 0) {
            description = "Overview of Registrations";
            barEntriesW = dashboard.getRegsWeekly().getBarEntries();
            barLabelsW = dashboard.getWeekDates();
            barEntriesQ = dashboard.getRegsMonthly().getBarEntries();
            barLabelsQ = dashboard.getQuarterDates();
        } else if (MODE == 1) {
            description = "Overview of Visits";
            barEntriesW = dashboard.getVisitsWeekly().getBarEntries();
            barLabelsW = dashboard.getWeekDates();
            barEntriesQ = dashboard.getVisitsMonthly().getBarEntries();
            barLabelsQ = dashboard.getQuarterDates();
        } else {
            barEntriesW = new ArrayList<>();
            barLabelsW = new ArrayList<>();
            barEntriesQ = new ArrayList<>();
            barLabelsQ = new ArrayList<>();
            barChart.setDescription("Error");
        }
        BarDataSet barDataSetW = new BarDataSet(barEntriesW, "Weekly");
        barDataSetW.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSetW.setBarSpacePercent(55);
        barDataW = new BarData(barLabelsW, barDataSetW);

        BarDataSet barDataSetQ = new BarDataSet(barEntriesQ, "Monthly");
        barDataSetQ.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSetQ.setBarSpacePercent(55);
        barDataQ = new BarData(barLabelsQ, barDataSetQ);
    }

    private void updateBarData(BarData barData, String s) {
        s = s + " " + description;
        barChart.setDescription(s);
        barChart.setData(barData);
        barChart.notifyDataSetChanged();
        barChart.animateY(2100);
        barChart.invalidate();
    }

    @Override
    public void onCheckedChanged(RadioGroup chipGroup, int i) {
        switch (i) {
            case R.id.radioButton1:
                radioButton2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                radioButton1.setTextColor(getResources().getColor(R.color.colorTextLight));
                updateBarData(barDataW, "Weekly");
                break;

            case R.id.radioButton2:
                radioButton1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                radioButton2.setTextColor(getResources().getColor(R.color.colorTextLight));
                updateBarData(barDataQ, "Monthly");
                break;
        }
    }
}