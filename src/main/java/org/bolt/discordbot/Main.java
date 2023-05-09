package org.bolt.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.sticker.StickerSnowflake;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bolt.discordbot.birthday.BirthdayManager;
import org.jetbrains.annotations.NotNull;
import org.quartz.SchedulerException;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;


public class Main extends ListenerAdapter
{
    private static JDA globalJDA;
    public static void main( String[] args ) throws LoginException, InterruptedException, SchedulerException, IOException {
//        int daysUntilXMAS = ChristmasCountdown.getDaysUntil(Calendar.DECEMBER, 25);
        // ChristmasCountdown xmas = new ChristmasCountdown();
        JDA jda = JDABuilder.createDefault(getField("bot.token"))
//                        .setActivity(Activity.playing(daysUntilXMAS + " Days until XMas"))
                // .setActivity(Activity.playing(xmas.days() + " days, " + xmas.hours() + " hours, " + xmas.minutes() + " minutes, " + xmas.seconds() + " seconds until Christmas"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableCache(CacheFlag.EMOJI)
                .addEventListeners(new Main(), new CustomCommands())

                                .build();

        globalJDA = jda;

        // ChristmasCountdown.statusChanger();


        BirthdayManager bdayM = new BirthdayManager();
        bdayM.startJob();
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        String msg = event.getMessage().getContentRaw().toLowerCase();
        if(msg.contains("get") && msg.contains("<@616280451962765312>"))
        {
            User target = event.getMessage().getReferencedMessage().getAuthor();
            String payload = String.format("yo %s", target.getAsMention());
            event.getMessage().reply(payload).queue();

        }
    }


    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event)
    {
        if(event.getGuild().getId().equals(getField("mainGuild")))
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TextChannel textChannel = event.getGuild().getTextChannelById(getField("joinChannel"));
            assert textChannel != null;
            textChannel.getHistory().retrievePast(1)
                    .map(messages -> messages.get(0)) // this assumes that the channel has at least 2 messages
                    .queue(message -> { // success callback
//                        System.out.println("The second most recent message is: " + message.getContentDisplay());
                        int count = 1;
                        if(message.getAuthor().isBot())
                        {
                            return;
                        }
                        while(count<=5)
                        {
                            message.replyFormat("https://distok.top/stickers/754103543786504244/754108890559283200-small.gif").queue();
                            // message.replyStickers(StickerSnowflake.fromId("749054660769218631")).queue();
                            count++;
                        }

                    });

        }

    }

    public static JDA getBotInstance()
    {
        return globalJDA;
    }

    public static String getField(String field)
    {
        Properties properties = new Properties();
        String fileName = "config.txt";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
        } catch (FileNotFoundException ex) {
            System.out.println("getPropety() FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("getProperty IOException");
        }
        return(properties.getProperty(field));
    }
}
