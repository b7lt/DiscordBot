package org.bolt.discordbot.birthday;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bolt.discordbot.Main;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BirthdayJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.out.println("exe");
            JDA jda = Main.getBotInstance();
            Guild server = jda.getGuildById("648687697090576414");
            Role birthdayRole = jda.getRoleById("820886568336097320");
            TextChannel textChannel = jda.getTextChannelById("759472980572373004");
            assert textChannel != null;
            assert birthdayRole != null;
            assert server != null;

            //remove people that have birthday role
            List<Member> membersWithRole = server.getMembersWithRoles(birthdayRole);
            for(Member m : membersWithRole)
            {
                server.removeRoleFromMember(m, birthdayRole).queue();
            }

            //give everyone role & ping
            BirthdayManager bdayM = new BirthdayManager();
            ArrayList<String> userIds = bdayM.todaysBirthdays();

            for (String userId : userIds) {
                if (textChannel.canTalk()) {
                    textChannel.sendMessage("@everyone Happy Birthday <@!" + userId + ">! :partying_face::partying_face:").queue();
                }
                server.addRoleToMember(jda.getUserById(userId), birthdayRole).queue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
