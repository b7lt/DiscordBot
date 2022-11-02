package org.bolt.discordbot;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import org.bolt.discordbot.birthday.BirthdayManager;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.Objects;

public class Commands extends ListenerAdapter
{
    @Override
    public void onSlashCommand(SlashCommandEvent event)
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
                    event.reply("Pinged all with nothing").setEphemeral(true).queue();
                }
            break;
            }

            case "kys": {
                Member member = event.getMember();
                event.getGuild().kick(member, "suicide").queue();
                break;
            }

            case "emotesteal":
            {
                String bruh = event.getOption("emote").getAsString();
                String emoteName = bruh.substring(2, bruh.indexOf(":", 2));
//                System.out.println(emoteName);
                String emoteId = bruh.substring(bruh.indexOf(":", 2)+1, bruh.length()-1);
//                String emoteId = bruh.substring(2, bruh.length()-1);
//                System.out.println(emoteId);
//                Emote emote = event.getJDA().getEmoteById(emoteId);

                try {
//                    java.net.URL url = new java.net.URL(emote.getImageUrl());
                    String link = "https://cdn.discordapp.com/emojis/" + emoteId + ".webp?size=240&quality=lossless";
                    java.net.URL url = new java.net.URL(link);
                    InputStream is = url.openStream();
                    event.getGuild().createEmote(emoteName, Icon.from(is)).queue();
                    event.reply("added " + bruh + " (probaby)").setEphemeral(true).queue();
                } catch (IOException e) {
                    event.reply("done fucked up").setEphemeral(true).queue();
                    throw new RuntimeException(e);
                }
                break;
            }
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    private void test(SlashCommandEvent event, String userName)
    {
        event.reply("Hello, " + userName).queue();
    }
}
