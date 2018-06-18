package io.github.sher1234.service.model.base;

import com.github.mikephil.charting.data.BarEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class Weekly implements Serializable {
    private String Total;
    private String Week1;
    private String Week2;
    private String Week3;
    private String Week4;

    Weekly(String total, String week1, String week2, String week3, String week4) {
        Total = total;
        Week1 = week1;
        Week2 = week2;
        Week3 = week3;
        Week4 = week4;
    }

    public String getTotal() {
        return Total;
    }

    public String getWeek4() {
        return Week4;
    }

    public String getWeek3() {
        return Week3;
    }

    public String getWeek2() {
        return Week2;
    }

    public String getWeek1() {
        return Week1;
    }

    public float getW4() {
        return Float.parseFloat(Week4 + "f");
    }

    public float getW3() {
        return Float.parseFloat(Week3 + "f");
    }

    public float getW2() {
        return Float.parseFloat(Week2 + "f");
    }

    public float getW1() {
        return Float.parseFloat(Week1 + "f");
    }

    public List<BarEntry> getBarEntries() {
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(getW1(), 0));
        barEntries.add(new BarEntry(getW2(), 1));
        barEntries.add(new BarEntry(getW3(), 2));
        barEntries.add(new BarEntry(getW4(), 3));
        return barEntries;
    }
}


