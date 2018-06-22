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

    Time(String total, String time1, String time2, String time3, String time4) {
        Total = total;
        Time1 = time1;
        Time2 = time2;
        Time3 = time3;
        Time4 = time4;
    }

    public String getTotal() {
        return Total;
    }

    public String getTime4() {
        return Time4;
    }

    public String getTime3() {
        return Time3;
    }

    public String getTime2() {
        return Time2;
    }

    public String getTime1() {
        return Time1;
    }

    public float getT4() {
        return Float.parseFloat(Time4 + "f");
    }

    public float getT3() {
        return Float.parseFloat(Time3 + "f");
    }

    public float getT2() {
        return Float.parseFloat(Time2 + "f");
    }

    public float getT1() {
        return Float.parseFloat(Time1 + "f");
    }

    public List<BarEntry> getBarEntries() {
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(getT1(), 0));
        barEntries.add(new BarEntry(getT2(), 1));
        barEntries.add(new BarEntry(getT3(), 2));
        barEntries.add(new BarEntry(getT4(), 3));
        return barEntries;
    }
}

