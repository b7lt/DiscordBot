package org.bolt.discordbot.birthday;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Birthday
{
    private int day;
    private int month;
    private String id;
    public Birthday(int myMonth, int myDay, String myId)
    {
        month = myMonth;
        day = myDay;
        id = myId;
    }

    //ADD FEATURE TO AUTO GIVE BIRTHDAY ROLE, REMOVE AT END OF DAY TOO MAYBE ?
    //maybe create a new cron through the birthday that executes day later, make sure it only executes once though

    public void happyBirthday() throws SchedulerException
    {
        JobDetail job = JobBuilder.newJob(BirthdayJob.class)
                .withIdentity(id, "Birthdays")
                .usingJobData("userId", id)
                .build();

        String cron = String.format("0 0 0 %d %d ? *", day, month);
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        SchedulerFactory schFactory = new StdSchedulerFactory();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(job, trigger);

    }

}
