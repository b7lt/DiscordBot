package org.bolt.discordbot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class ChristmasCountdown
{
    private long diff;
    public ChristmasCountdown()
    {
        String dateStop = "21/12/25 00:00:00";

        // Custom date format
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d2 = null;
        try { d2 = format.parse(dateStop); } catch (ParseException e) { e.printStackTrace(); }
        Date today = new Date();

        diff = d2.getTime() - today.getTime();
    }

    public long seconds()
    {
        return( TimeUnit.MILLISECONDS.toSeconds(diff) % 60 );
    }
    public long minutes()
    {
        return( TimeUnit.MILLISECONDS.toMinutes(diff) % 60 );
    }
    public long hours()
    {
        return( TimeUnit.MILLISECONDS.toHours(diff) % 24 );
    }
    public long days()
    {
        return( TimeUnit.MILLISECONDS.toDays(diff) );
    }
}