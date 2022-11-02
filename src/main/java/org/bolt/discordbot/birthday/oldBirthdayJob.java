package org.bolt.discordbot.birthday;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bolt.discordbot.Main;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class oldBirthdayJob implements Job {

    public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
        JDA jda = Main.getBotInstance();
        TextChannel textChannel = jda.getTextChannelById("907636473577218070");
        assert textChannel != null;
        String id = jExeCtx.getJobDetail().getJobDataMap().getString("userId");
//        String message = String.format("Happy bday <@!%s>", id);
        if(textChannel.canTalk()) {
            textChannel.sendMessage(id).queue();
        }
        System.out.println("Birthday job for " + id + " ran successfully");
    }
}
