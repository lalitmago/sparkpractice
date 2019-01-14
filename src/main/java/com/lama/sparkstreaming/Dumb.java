package com.lama.sparkstreaming;

import java.util.*;
import java.lang.Integer;

class Dumb {
    public static List<String> dows = Collections.unmodifiableList(Arrays.asList("SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"));

    public static String getDay(String day, String month, String year) {
        // get the supported ids for GMT-08:00 (Pacific Standard Time)
        String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
        // if no ids were returned, something is wrong. get out.
        if (ids.length == 0)
            System.exit(0);
        // create a Pacific Standard Time time zone
        SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
        // set up rules for Daylight Saving Time
        pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

        // create a GregorianCalendar with the Pacific Daylight time zone
        // and the desired date and time
        Calendar calendar = new GregorianCalendar(pdt);
        calendar.set(Calendar.MONTH, Integer.valueOf(month));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));


        calendar.set(Calendar.YEAR, Integer.valueOf(year));

        // print out a bunch of interesting things
        System.out.println("SUNDAY: " + calendar.SUNDAY);
        System.out.println("MONDAY: " + calendar.MONDAY);
        System.out.println("TUESDAY: " + calendar.TUESDAY);
        System.out.println("WEDNESDAY: " + calendar.WEDNESDAY);
        System.out.println("THURSDAY: " + calendar.THURSDAY);
        System.out.println("FRIDAY: " + calendar.FRIDAY);
        System.out.println("SATURDAY: " + calendar.SATURDAY);
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("Dow value : " + dow);
        return dows.get(dow-1);
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String month = in.next();
        String day = in.next();
        String year = in.next();

        System.out.println(getDay(day, month, year));
    }
}