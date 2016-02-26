package com.basket.basketmanager.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateFormatter {

    public static final String PATTERN_DATE = "dd/MM/yyyy";
    public static final String PATTERN_TIME = "HH:mm";
    public static final String PATTERN_DATETIME = PATTERN_DATE + " - " + PATTERN_TIME;
    public static final int DAYS_IN_MILLIS = 1000 * 60 * 60 * 24;

    public static String formatDate(GregorianCalendar calendar) {
        GregorianCalendar now = new GregorianCalendar();
        if (now.get(Calendar.YEAR) != calendar.get(Calendar.YEAR) || calendar.compareTo(now) < 0) {
            return new SimpleDateFormat(PATTERN_DATE).format(calendar.getTime());
        } else {
            int diff = daysBetween(now.getTime(), calendar.getTime());
            String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            if (diff < 7) {
               if (now.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                   return "Oggi";
               } else {
                   now.add(Calendar.DAY_OF_MONTH, 1);
                   if (now.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                       return "Domani";
                   } else {
                       String weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                       return capitalize(weekday) + " " + day;
                   }
               }
            } else {
                String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                return day + " " + capitalize(month);
            }
        }
    }

    public static String formatDateExtended(GregorianCalendar calendar) {
        return new SimpleDateFormat(PATTERN_DATE).format(calendar.getTime());
    }

    public static GregorianCalendar parseDateTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATETIME);
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(formatter.parse(date));
            return calendar;
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public static String formatDateTime(GregorianCalendar calendar) {
        return new SimpleDateFormat(PATTERN_DATETIME).format(calendar.getTime());
    }

    public static String formatTime(GregorianCalendar calendar) {
        return new SimpleDateFormat(PATTERN_TIME).format(calendar.getTime());
    }

    private static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DAYS_IN_MILLIS);
    }

    private static String capitalize(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
