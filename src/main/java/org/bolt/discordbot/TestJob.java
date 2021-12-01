package org.bolt.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {


    public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
        System.out.println("TestJob run successfully...");
        JDA jda = Main.getBotInstance();
        TextChannel textChannel = jda.getTextChannelById("759458699261247490");
        assert textChannel != null;
        if(textChannel.canTalk()) {
            textChannel.sendMessage("@everyone").queue();
        }
    }

}