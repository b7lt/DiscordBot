package org.bolt.discordbot;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Schedulers
{
    public static void timer() throws SchedulerException {
        // specify the job' s details..
        JobDetail job = JobBuilder.newJob(TestJob.class)
                .withIdentity("testJob")
                .build();

        // specify the running period of the job
        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build();

        //schedule the job
        SchedulerFactory schFactory = new StdSchedulerFactory();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(job, trigger);
    }

    public static void cronJob() throws SchedulerException
    {
        JobDetail job = JobBuilder.newJob(TestJob.class)
                .withIdentity("testJob")
                .build();


        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 56 1 12 NOV ? *")) // 1 30 am
                .build();

        SchedulerFactory schFactory = new StdSchedulerFactory();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(job, trigger);

    }
}
