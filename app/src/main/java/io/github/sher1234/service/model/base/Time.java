package io.github.sher1234.service.model.base;

import com.github.mikephil.charting.data.BarEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class Time implements Serializable {

    private String Total;
    private String Time1;
    private String Time2;
    private String Time3;
    private String Time4;
    private String Time5;
    private String Time6;
    private String Time7;
    private String Time8;
    private String Time9;
    private String Time10;
    private String Time11;
    private String Time12;

    Time(String total, String time1, String time2, String time3, String time4) {
        Total = total;
        Time1 = time1;
        Time2 = time2;
        Time3 = time3;
        Time4 = time4;
    }

    public float getT(String s) {
        return Float.parseFloat(s + "f");
    }

    public List<BarEntry> getBarEntriesMonthly() {
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(getT(Time12), 0));
        barEntries.add(new BarEntry(getT(Time11), 1));
        barEntries.add(new BarEntry(getT(Time10), 2));
        barEntries.add(new BarEntry(getT(Time9), 3));
        barEntries.add(new BarEntry(getT(Time8), 4));
        barEntries.add(new BarEntry(getT(Time7), 5));
        barEntries.add(new BarEntry(getT(Time6), 6));
        barEntries.add(new BarEntry(getT(Time5), 7));
        barEntries.add(new BarEntry(getT(Time4), 8));
        barEntries.add(new BarEntry(getT(Time3), 9));
        barEntries.add(new BarEntry(getT(Time2), 10));
        barEntries.add(new BarEntry(getT(Time1), 11));
        return barEntries;
    }

    public List<BarEntry> getBarEntriesDaily() {
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(getT(Time7), 0));
        barEntries.add(new BarEntry(getT(Time6), 1));
        barEntries.add(new BarEntry(getT(Time5), 2));
        barEntries.add(new BarEntry(getT(Time4), 3));
        barEntries.add(new BarEntry(getT(Time3), 4));
        barEntries.add(new BarEntry(getT(Time2), 4));
        barEntries.add(new BarEntry(getT(Time1), 6));
        return barEntries;
    }
}

