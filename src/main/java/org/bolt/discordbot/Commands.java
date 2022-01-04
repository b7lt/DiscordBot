package org.bolt.discordbot;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bolt.discordbot.birthday.BirthdayManager;

import java.io.IOException;
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
            case "addbirthday": {
                String userId = event.getOption("user").getAsMember().getId();
                int month = (int) event.getOption("month").getAsLong();
                int day = (int) event.getOption("day").getAsLong();
                try {
                    BirthdayManager bdayM = new BirthdayManager();
                    bdayM.addBirthday(userId, month, day);
                    event.reply("Added <@!" + userId + ">'s birthday").setEphemeral(true).queue();
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
            case "updatestatus": {
                String newStatus = Objects.requireNonNull(event.getOption("newstatus")).getAsString();
                event.getJDA().getPresence().setActivity(Activity.playing(newStatus));
                event.reply("Updated the bot's status to " + newStatus).setEphemeral(true).queue();
                System.out.println(event.getUser().getName() + " updated the bot's status to " + newStatus);
                break;
            }
            case "ghostping": {
                String userId = event.getOption("idiot").getAsMember().getId();
                event.getMessageChannel().sendMessage("<@!" + userId + ">").queue(message -> message.delete().queue());
                event.reply("done").setEphemeral(true).queue();
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
