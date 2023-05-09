package org.bolt.discordbot;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bolt.discordbot.birthday.BirthdayManager;
import org.bolt.discordbot.leetcode.LeetcodeScrape;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.Objects;

public class CustomCommands extends ListenerAdapter
{
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event)
    {
        CommandListUpdateAction commands = event.getJDA().updateCommands();

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


        OptionData difficulty = new OptionData(OptionType.STRING, "difficulty", "Which difficulty? (if left blank, random difficulty)", false)
                .addChoice("Easy", "easy")
                .addChoice("Medium", "medium")
                .addChoice("Hard", "hard");
        OptionData paid = new OptionData(OptionType.BOOLEAN, "paid", "Include paid questions? (if left blank, only free are included)", false);
        commands.addCommands(
                Commands.slash("leetcode", "Get a random leetcode problem")
                        .addOptions(difficulty, paid)
        );

        commands.queue(); //queue it to discords servers
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if(event.getGuild() == null)
            return;
        switch(event.getName())
        {
            case "test": {
                String userName = event.getMember().getEffectiveName();
                test(event, userName);
                break;
            }
            case "setbirthday": {
                String userId = event.getOption("user").getAsMember().getId();
                int month = (int) event.getOption("month").getAsLong();
                int day = (int) event.getOption("day").getAsLong();
                try {
                    BirthdayManager bdayM = new BirthdayManager();
                    bdayM.addBirthday(userId, month, day);
                    event.reply("Set <@!" + userId + ">'s birthday to " + month + "/" + day + " in the database.").setEphemeral(true).queue();
                } catch (IOException e) {
                    e.printStackTrace();
                    event.reply("Failed to add birthday").setEphemeral(true).queue();
                }
                break;
            }
            case "removebirthday": {
                String userId = event.getOption("user").getAsMember().getId();
                try {
                    BirthdayManager bdayM = new BirthdayManager();
                    bdayM.removeBirthday(userId);
                    event.reply("Removed <@!" + userId + ">'s birthday").setEphemeral(true).queue();
                } catch (IOException e) {
                    e.printStackTrace();
                    event.reply("Failed to remove birthday").setEphemeral(true).queue();
                }
                break;
            }
            case "getbirthday": {
                String userId = event.getOption("user").getAsMember().getId();
                try {
                    BirthdayManager bdayM = new BirthdayManager();
                    try {
                        String date = bdayM.getBirthday(userId);
                        event.reply("<@!" + userId + ">'s birthday is on " + date).setEphemeral(true).queue();
                    }
                    catch(JSONException e) {
                        event.reply("<@!" + userId + "> does not have a set birthday").setEphemeral(true).queue();
                    }
                } catch (IOException e) {
                    event.reply("this shit completely broke lol").setEphemeral(true).queue();
                    e.printStackTrace();
                }
                break;
            }
            case "updatestatus": {
                String newStatus = Objects.requireNonNull(event.getOption("newstatus")).getAsString();
                event.getJDA().getPresence().setActivity(Activity.playing(newStatus));
                event.reply("Updated the bot's status to " + newStatus).setEphemeral(true).queue();
                System.out.println(event.getUser().getName() + " updated the bot's status to " + newStatus);
                break;
            }
            case "ghostping": {
                String userId = event.getOption("idiot").getAsMember().getId();
//                event.getMessageChannel().sendMessage("<@!" + userId + ">").queue(message -> message.delete().queue());
                event.reply("<@!" + userId + ">").queue(m -> m.deleteOriginal().queue());
                System.out.println(event.getUser().getName() + " ghost-pinged " + event.getOption("idiot").getAsMember().getEffectiveName());
                break;
            }

            case "pingall": {
                try {
                    String input = event.getOption("msg").getAsString();
                    String message = input;
                    for (Member member : event.getGuild().getMembers()) {
                        message = message + "<@!" + member.getId() + ">";
                        if (message.length() > 1600) {
                            event.getChannel().sendMessage(message).queue();
                            message = input;
                        }
                    }
                    event.getChannel().sendMessage(message).queue();
                    System.out.println(event.getUser().getName() + " pinged all with " + input);
                    event.reply("Pinged all with " + input).setEphemeral(false).queue();
                }
                catch(NullPointerException e)
                {
                    String message = "";
                    for(Member member : event.getGuild().getMembers()) {
                        message = message + "<@!" + member.getId() + ">";
                        if (message.length() > 1600) {
                            event.getChannel().sendMessage(message).queue();
                            message = "";
                        }
                    }
                    event.getChannel().sendMessage(message).queue();
                    System.out.println(event.getUser().getName() + " pinged all");
                    event.reply("Pinged all with nothing").setEphemeral(false).queue();
                }
            break;
            }

            case "kys": {
                Member member = event.getMember();
                event.getGuild().kick(member).queue();
                break;
            }

            case "emotesteal":
            {
                String bruh = event.getOption("emote").getAsString();
                String emoteName = bruh.substring(2, bruh.indexOf(":", 2));
                boolean animated = false;
//                System.out.println(emoteName);
                String emoteId = bruh.substring(bruh.indexOf(":", 2)+1, bruh.length()-1);
                if(bruh.substring(1, 2).equals("a"))
                {
                    emoteName = bruh.substring(3, bruh.indexOf(":", 3));
                    animated = true;
                    emoteId = bruh.substring(bruh.indexOf(":", 3)+1, bruh.length()-1);
                }

                try {
//                    java.net.URL url = new java.net.URL(emote.getImageUrl());
                    String link = "https://cdn.discordapp.com/emojis/" + emoteId + ".webp?size=2048&quality=lossless";
                    if(animated)
                    {
                        link = "https://cdn.discordapp.com/emojis/" + emoteId + ".gif?size=2048&quality=lossless";
                    }
                    java.net.URL url = new java.net.URL(link);
                    InputStream is = url.openStream();
                    event.getGuild().createEmoji(emoteName, Icon.from(is)).queue();
                    String returnz;
                    if(animated){
                        returnz = "<a:" + emoteName + ":" + emoteId + ">";
                    }
                    else {
                        returnz = "<:" + emoteName + ":" + emoteId + ">";
                    }
                    event.reply("added " + returnz + " (probaby)").setEphemeral(false).queue();
                } catch (IOException e) {
                    event.reply("done fucked up").setEphemeral(false).queue();
                    throw new RuntimeException(e);
                }
                break;
            }
            case "leetcode":
            {
                boolean paid = event.getOption("paid").getAsBoolean();
                String difficulty = event.getOption("difficulty").getAsString();
                JSONObject test = LeetcodeScrape.leetcodeQuestion(difficulty, paid);
                String PROBLEMS_BASEURL = "https://leetcode.com/problems/";
                event.reply(PROBLEMS_BASEURL + test.getJSONObject("stat").getString("question__title_slug")).queue();
            }
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    private void test(SlashCommandInteractionEvent event, String userName)
    {
        event.reply("Hello, " + userName).queue();
    }
}
