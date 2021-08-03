package ir.bvar.imenfood.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;

/**
 * Created by rezapilehvar on 7/2/2018 AD.
 */

public class TimeUtility {
    public static DateTime getTimeFromBullSheetServerString(String date, String time) {
        if (date == null || time == null) {
            return null;
        }

        try {
            DateTime dateTime = new DateTime();

            String[] splicedDate = date.split("-");

            int year;
            int month;
            int day;

            if (splicedDate.length == 3) {
                year = Integer.parseInt(splicedDate[0]);
                month = Integer.parseInt(splicedDate[1]);
                day = Integer.parseInt(splicedDate[2]);
            } else {
                return null;
            }

            dateTime = dateTime.withYear(year);
            dateTime = dateTime.withMonthOfYear(month);
            dateTime = dateTime.withDayOfMonth(day);

            String[] splicedTime = time.split(":");

            int hourOfDay;
            int minute;

            if (splicedTime.length >= 2) {
                hourOfDay = Integer.parseInt(splicedTime[0]);
                minute = Integer.parseInt(splicedTime[1]);
            } else {
                return null;
            }

            dateTime = dateTime.withHourOfDay(hourOfDay);
            dateTime = dateTime.withMinuteOfHour(minute);

            return dateTime;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getPeriodTextFromCalendar(DateTime serverDateTime){
        if(serverDateTime == null){
            return null;
        }

        long timeStamp = serverDateTime.getMillis();

        if(isToday(timeStamp)){
            return "امروز ساعت " + serverDateTime.get(DateTimeFieldType.hourOfDay()) + ":" + serverDateTime.get(DateTimeFieldType.minuteOfHour());
        }else if(isYesterday(timeStamp)){
            return "دیروز ساعت " + serverDateTime.get(DateTimeFieldType.hourOfDay()) + ":" + serverDateTime.get(DateTimeFieldType.minuteOfHour());
        }else {
            return getDaysAgo(serverDateTime,true);
        }
    }

    public static boolean isToday(long timeStamp) {
        DateTime endOfToday = new DateTime(System.currentTimeMillis()).plusDays(1).withTimeAtStartOfDay();
        DateTime startOfToday = new DateTime(System.currentTimeMillis()).withTimeAtStartOfDay();

        return timeStamp < endOfToday.getMillis() && timeStamp > startOfToday.getMillis();
    }

    public static boolean isYesterday(long timeStamp) {
        DateTime startOfToday = new DateTime(System.currentTimeMillis()).withTimeAtStartOfDay();
        DateTime startOfYesterday = new DateTime(System.currentTimeMillis()).minusDays(1).withTimeAtStartOfDay();

        return timeStamp > startOfYesterday.getMillis() && timeStamp < startOfToday.getMillis();
    }

    public static String getDaysAgo(DateTime serverDateTime, boolean checkTime) {
        DateTime now = new DateTime(System.currentTimeMillis());

        int days;

        if (checkTime) {
            days = Days.daysBetween(serverDateTime,now).getDays();
        } else {
            days = Days.daysBetween(serverDateTime.withTimeAtStartOfDay(),now.withTimeAtStartOfDay()).getDays();
        }


        if (days == 1) {
            return "دیروز";
        } else {
            return days + " روز قبل";
        }
    }
}
