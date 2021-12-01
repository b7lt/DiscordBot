package org.bolt.discordbot;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChristmasCountdown
{
    public static int getDaysUntil(int month, int day) {

        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int desiredDay = calendar.get(Calendar.DAY_OF_YEAR);

        int difference = desiredDay - today;

        if (difference <= 0) {

            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            int daysUntilEnd = calendar.get(Calendar.DAY_OF_YEAR) - today;


            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            desiredDay = calendar.get(Calendar.DAY_OF_YEAR);

            difference = daysUntilEnd + desiredDay;
        }

        return difference;
    }
    public static String newGetTimeUntil(int month, int day)
    {
        Calendar today = Calendar.getInstance();
        Calendar target = new GregorianCalendar();

        target.set(Calendar.MONTH, month);
        target.set(Calendar.DAY_OF_MONTH, day);

        long diffInMilli = target.getTimeInMillis() - today.getTimeInMillis();
        return("Result: " + diffInMilli);


    }

}
