package io.github.sher1234.service.model.base;

import com.github.mikephil.charting.data.BarEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class Quarterly implements Serializable {
    private String Total;
    private String Quarter1;
    private String Quarter2;
    private String Quarter3;
    private String Quarter4;

    Quarterly(String total, String quarter1, String quarter2, String quarter3, String quarter4) {
        Total = total;
        Quarter1 = quarter1;
        Quarter2 = quarter2;
        Quarter3 = quarter3;
        Quarter4 = quarter4;
    }

    public String getTotal() {
        return Total;
    }

    public String getQuarter4() {
        return Quarter4;
    }

    public String getQuarter3() {
        return Quarter3;
    }

    public String getQuarter2() {
        return Quarter2;
    }

    public String getQuarter1() {
        return Quarter1;
    }

    public float getQ4() {
        return Float.parseFloat(Quarter4 + "f");
    }

    public float getQ3() {
        return Float.parseFloat(Quarter3 + "f");
    }

    public float getQ2() {
        return Float.parseFloat(Quarter2 + "f");
    }

    public float getQ1() {
        return Float.parseFloat(Quarter1 + "f");
    }

    public List<BarEntry> getBarEntries() {
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(getQ1(), 0));
        barEntries.add(new BarEntry(getQ2(), 1));
        barEntries.add(new BarEntry(getQ3(), 2));
        barEntries.add(new BarEntry(getQ4(), 3));
        return barEntries;
    }
}