package dev.mrflyn.writerbot;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dev.mrflyn.writerbot.Database.GuildConfigCache;
import dev.mrflyn.writerbot.Database.Updater;
import dev.mrflyn.writerbot.apis.API;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Timer;

public class Listeners extends ListenerAdapter {
//    URIBuilder uri = new URIBuilder("https://api.paste.gg/v1/pastes");
    URIBuilder uri = new URIBuilder("https://paste.helpch.at/documents");
    Gson gson = new Gson();

    public Listeners() throws URISyntaxException {
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing(" in "+event.getGuildTotalCount()+" Servers."));
        GuildConfigCache.loadAllCaches();
        Timer t = new Timer("Timer");
        t.schedule(new Updater(), 300000L,300000L);
    }

    @Override
    public void onGuildReady(GuildReadyEvent event){

    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if(event.getGuild()==null)return;
        if (event.getMember()==null)return;
        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR))return;
        GuildConfigCache cache = GuildConfigCache.loadedCaches.get(event.getGuild().getIdLong());
        switch (event.getName()){
            case "activatedapi":
                String option = event.getOptions().get(0).getAsString();
                if(!API.getAllApis().contains(option)){
                    event.reply("Invalid Service-Name.").setEphemeral(true).queue();
                    return;
                }
                cache.setActivatedApi(API.valueOf(option));
                event.reply("Successfully set Service to: "+option+".").queue();
                break;
            case "failsilently":
                boolean fs = event.getOptions().get(0).getAsBoolean();
                cache.setFailSilently(fs);
                event.reply("Successfully set Fail-Silently to: "+fs+".").queue();
                break;
            case "autodelete":
                boolean ad = event.getOptions().get(0).getAsBoolean();
                cache.setAutoDelete(ad);
                event.reply("Successfully set Auto-Delete to: "+ad+".").queue();
                break;

        }

    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing(" in "+event.getJDA().getGuilds().size()+" Servers."));
        GuildConfigCache.onGuildJoin(event.getGuild().getIdLong());
    }
    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing(" in "+event.getJDA().getGuilds().size()+" Servers."));
    }



    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getAuthor().getId().equals(Bot.jda.getSelfUser().getId())) return;
        GuildConfigCache cache = GuildConfigCache.loadedCaches.get(e.getGuild().getIdLong());
        for (Message.Attachment attachment : e.getMessage().getAttachments()) {
            if (attachment.isImage() || attachment.isVideo() || attachment.isSpoiler()) continue;
            attachment.retrieveInputStream().thenAccept(stream -> {
//                System.out.println(Main.encodeStreamToBase64(stream));
//                Paste file = new Paste(
//                        "Content By "+e.getAuthor().getAsTag()+" ("+e.getAuthor().getId()+")",
//                        "Made with WriterBot. Join at https://discord.vectlabs.xyz",
//                        "unlisted",
//                        List.of(new PasteFile(attachment.getFileName(),
//                                new PasteFileContent(
//                                        "text",
//                                        "null",
//                                        Main.streamToString(stream)
//                                )))
//                );
//                System.out.println(gson.toJson(file));
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    client.sendAsync(cache.getActivatedApi().getWrapper().post(stream, attachment.getFileName(), e.getAuthor()),
                            HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {

                        System.out.println(response.body());

                        if (response.statusCode() == cache.getActivatedApi().getWrapper().success()) {
                            if(cache.isAutoDelete())
                                e.getMessage().editMessage(e.getMessage().getContentRaw()).queue();
                            e.getMessage().reply(attachment.getFileName()
                                    + " by " + e.getAuthor().getAsMention() + ":  " + cache.getActivatedApi().getWrapper().getLink(response)).queue();

                        } else {
                            if (!cache.isFailSilently())
                                e.getMessage().reply("Failed to create paste.").queue();
                            System.out.println("Debug-response-json: " + response.body());
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

}
