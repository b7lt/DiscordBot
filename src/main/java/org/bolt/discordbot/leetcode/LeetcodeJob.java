package org.bolt.discordbot.leetcode;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bolt.discordbot.Main;
import org.bolt.discordbot.birthday.BirthdayJob;
import org.json.JSONObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.awt.*;

public class LeetcodeJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JDA jda = Main.getBotInstance();
        TextChannel textChannel = jda.getTextChannelById("1105425014527893585");
        assert textChannel != null;

        int difficulty = Integer.parseInt(Main.getField("lcDifficulty"));
        JSONObject qu = LeetcodeScrape.leetcodeQuestion(difficulty, false);
        String PROBLEMS_BASEURL = "https://leetcode.com/problems/";
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("%d. %s", qu.getJSONObject("stat").getInt("frontend_question_id"), qu.getJSONObject("stat").getString("question__title")), PROBLEMS_BASEURL + qu.getJSONObject("stat").getString("question__title_slug"));
        eb.setDescription("⭐ DAILY LEETCODE QUESTION! ⭐");
        eb.setColor(Color.MAGENTA);
        switch(difficulty)
        {
            case 1: {
                eb.addField("Difficulty", "Easy", true);
                break;
            }
            case 2: {
                eb.addField("Difficulty", "Medium", true);
                break;
            }
            case 3: {
                eb.addField("Difficulty", "Hard", true);
                break;
            }
        }
        eb.addField("Paid Question", String.valueOf(qu.getBoolean("paid_only")), true);
        eb.addBlankField(false);
        int submissions = qu.getJSONObject("stat").getInt("total_submitted");
        int acceptions = qu.getJSONObject("stat").getInt("total_acs");
        eb.addField("Submissions", String.valueOf(submissions), true);
        eb.addField("Acceptions", String.valueOf(acceptions), true);
        eb.addField("Acceptance Rate", String.valueOf((float) acceptions / submissions * 100) + "%", true);
        eb.setThumbnail("https://leetcode.com/static/images/LeetCode_logo_rvs.png");

        textChannel.sendMessage("<@&1105425064339451914> <a:alert:1105431711573094430> DAILY LEETCODE! <a:alert:1105431711573094430>")
                .addEmbeds(eb.build())
                .addActionRow(
                        Button.link(PROBLEMS_BASEURL + qu.getJSONObject("stat").getString("question__title_slug"), "Link to Problem"))
                .queue();

    }

    public static void startJob() throws SchedulerException
    {
        JobDetail job = JobBuilder.newJob(LeetcodeJob.class)
                .withIdentity("lc")
                .build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule("2 0 0 ? * * *"))
                .build();

        SchedulerFactory schFactory = new StdSchedulerFactory();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(job, trigger);
    }
}
