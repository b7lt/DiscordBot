package org.bolt.discordbot;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

public class ChristmasCountdown
{
    private long diff;
    public ChristmasCountdown()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String dateStop = year + "/12/25 00:00:00";

        // Custom date format
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d2 = null;
        try { d2 = format.parse(dateStop); } catch (ParseException e) { e.printStackTrace(); }
        Date today = new Date();

        diff = d2.getTime() - today.getTime();

        if(diff < 0)
        {
            year += 1;
            dateStop = year + "/12/25 00:00:00";
            d2 = null;
            try { d2 = format.parse(dateStop); } catch (ParseException e) { e.printStackTrace(); }
            today = new Date();

            diff = d2.getTime() - today.getTime();
        }
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

    public static void statusChanger() throws SchedulerException {
        // specify the job' s details..
        JobDetail job = JobBuilder.newJob(CCStatusChanger.class)
                .withIdentity("CCStatusChanger")
                .build();

        // specify the running period of the job
        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(12)
                        .repeatForever())
                .build();

        //schedule the job
        SchedulerFactory schFactory = new StdSchedulerFactory();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(job, trigger);
    }
    public static int randomTime()
    {
        return (int) ((Math.random() * 10) + 5);
    }
}
