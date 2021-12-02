package org.bolt.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CCStatusChanger implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JDA jda = Main.getBotInstance();
        ChristmasCountdown xmas = new ChristmasCountdown();
        jda.getPresence().setActivity(Activity.playing(xmas.days() + " days, " + xmas.hours() + " hours, " + xmas.minutes() + " minutes, " + xmas.seconds() + " seconds until Christmas"));
    }
}
