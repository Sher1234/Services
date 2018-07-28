package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class Time implements Serializable {

    private int Total;
    private int Time1;
    private int Time2;
    private int Time3;
    private int Time4;
    private int Time5;
    private int Time6;
    private int Time7;
    private int Time8;
    private int Time9;
    private int Time10;
    private int Time11;
    private int Time12;

    Time() {
    }

    public Integer[] getDaily() {
        return new Integer[]{Time1, Time2, Time3, Time4, Time5, Time6, Time7};
    }

    public Integer[] getMonthly() {
        return new Integer[]{Time1, Time2, Time3, Time4, Time5, Time6, Time7, Time8, Time9, Time10,
                Time11, Time12};
    }

    public List<Integer> getDailyList() {
        return Arrays.asList(getDaily());
    }

    public List<Integer> getMonthlyList() {
        return Arrays.asList(getMonthly());
    }
}

