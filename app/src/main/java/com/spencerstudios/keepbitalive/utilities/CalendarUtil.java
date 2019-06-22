package com.spencerstudios.keepbitalive.utilities;

import java.util.Calendar;

public class CalendarUtil {

    private Calendar calendar;

    public CalendarUtil() {
        calendar = Calendar.getInstance();
    }

    public int[] getDate() {
        return new int[]{
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        };
    }

    public long setDate(int... args) {
        calendar.set(Calendar.YEAR, args[0]);
        calendar.set(Calendar.MONTH, args[1]);
        calendar.set(Calendar.DAY_OF_MONTH, args[2]);
        calendar.set(Calendar.HOUR_OF_DAY, args[3]);
        calendar.set(Calendar.MINUTE, args[4]);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }
}
