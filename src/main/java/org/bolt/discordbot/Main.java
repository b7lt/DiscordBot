package org.bolt.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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

        //update cmd list
        CommandListUpdateAction commands = jda.updateCommands();

        //list of cmds
        commands.addCommands(
                Commands.slash("test", "Test a response")
        );

        commands.addCommands(
                Commands.slash("setbirthday", "Add/change someone's birthday to the database")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user whose birthday you wish to add").setRequired(true))
                        .addOptions(new OptionData(OptionType.INTEGER, "month", "The month of their birthday").setRequired(true))
                        .addOptions(new OptionData(OptionType.INTEGER, "day", "The day of their birthday").setRequired(true))
        );

        commands.addCommands(
                Commands.slash("removebirthday", "Remove someone's birthday from the database")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user whose birthday you wish to remove").setRequired(true))
        );

        commands.addCommands(
                Commands.slash("getbirthday", "Get someone's birthday (if they have it set on the bot)")
                        .addOptions(new OptionData(OptionType.USER, "user", "The user whose birthday you wish to get").setRequired(true))
        );

        commands.addCommands(
                Commands.slash("updatestatus", "Update the bot's status message")
                        .addOptions(new OptionData(OptionType.STRING, "newstatus", "The new status message").setRequired(true))
        );

        commands.addCommands(
                Commands.slash("ghostping", "ghost ping an idiot")
                        .addOptions(new OptionData(OptionType.USER, "idiot", "the idiot who u wanna ghost ping").setRequired(true))
        );

        commands.addCommands(
                Commands.slash("pingall", "pings everyone individually")
                        .addOptions(new OptionData(OptionType.STRING,"msg", "add something before all pings").setRequired(false))
        );

        commands.addCommands(
                Commands.slash("kys", "suicide")
        );

        commands.addCommands(
                Commands.slash("emotesteal", "steal an emote from another server")
                        .addOptions(new OptionData(OptionType.STRING, "emote", "steal this emote yo").setRequired(true))
        );

        commands.queue(); //queue it to discords servers

        //Schedulers.cronJob();
//        Birthday test = new Birthday(11, 16, "183774018883682305");
//        test.happyBirthday();
        BirthdayManager bdayM = new BirthdayManager();
        bdayM.startJob();
//        bdayM.addBirthday("fdffasf", 5, 20);
//        bdayM.updateBirthday("fdasf", 4, 15);
//        System.out.println("number of bday " + bdayM.numberOfBirthdays());
//        System.out.println(ChristmasCountdown.newGetTimeUntil(Calendar.DECEMBER, 25));
    }

//    @Override
//    public void onMessageReceived(@NotNull MessageReceivedEvent event)
//    {
//        if(event.isFromType(ChannelType.PRIVATE))
//            System.out.printf("[PM] %s: %s\n",
//                    event.getAuthor().getName(),
//                    event.getMessage().getContentDisplay()
//            );
//        else {
//            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
//                    event.getTextChannel().getName(), Objects.requireNonNull(event.getMember()).getEffectiveName(),
//                    event.getMessage().getContentDisplay());
////            if(event.getAuthor().getId().equals("247392250470858752")) event.getTextChannel().sendMessage("frank kekw").queue();
//        }
//    }
//
//    @Override
//    public void onMessageReceived(@NotNull MessageReceivedEvent event)
//    {
//        if(event.getAuthor().getId().equals("464507275189682176"))
//        {
//            event.getMessage().delete().queue();
//        }
//    }

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
