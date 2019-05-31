package io.github.sher1234.service.functions.v4;

import java.text.SimpleDateFormat;
import java.util.Locale;

public enum DateFormat {

    TimeAmPm(DateString.TimeAmPm),
    FullAmPm(DateString.FullAmPm),
    Time24h(DateString.Time24h),
    Full24h(DateString.Full24h),
    Date(DateString.Date);

    private final String format;

    DateFormat(String format) {
        this.format = format;
    }

    public String format(java.util.Date date) {
        return date == null? "":new SimpleDateFormat(format, Locale.US).format(date);
    }

    private interface DateString {
        String FullAmPm = "MMM dd, yyyy hh:mm a";
        String Full24h = "MMM dd, yyyy HH:mm";
        String TimeAmPm = "hh:mm a";
        String Time24h = "HH:mm";
        String Date = "dd-MM-yy";
    }
}