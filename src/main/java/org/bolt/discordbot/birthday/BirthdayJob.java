package org.bolt.discordbot.birthday;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bolt.discordbot.Main;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.ArrayList;

public class BirthdayJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            BirthdayManager bdayM = new BirthdayManager();
            ArrayList<String> userIds = bdayM.todaysBirthdays();
            JDA jda = Main.getBotInstance();
            TextChannel textChannel = jda.getTextChannelById("759472980572373004");
            assert textChannel != null;
            for(int i = 0; i < userIds.size(); i++)
            {
                if(textChannel.canTalk()) {
                    textChannel.sendMessage("@everyone Happy Birthday <@!" + userIds.get(i) + ">! :partying_face::partying_face:").queue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
