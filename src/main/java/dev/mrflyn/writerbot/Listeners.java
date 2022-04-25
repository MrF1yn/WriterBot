package dev.mrflyn.writerbot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.mrflyn.writerbot.pasteapi.Paste;
import dev.mrflyn.writerbot.pasteapi.PasteFile;
import dev.mrflyn.writerbot.pasteapi.PasteFileContent;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;

public class Listeners extends ListenerAdapter {
    URIBuilder uri = new URIBuilder("https://api.paste.gg/v1/pastes");
    Gson gson = new Gson();

    public Listeners() throws URISyntaxException {
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing(" in "+event.getGuildTotalCount()+" Servers."));
    }
    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing(" in "+event.getJDA().getGuilds().size()+" Servers."));
    }
    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing(" in "+event.getJDA().getGuilds().size()+" Servers."));
    }



    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getAuthor().getId().equals(Bot.jda.getSelfUser().getId())) return;
        for (Message.Attachment attachment : e.getMessage().getAttachments()) {
            if (attachment.isImage() || attachment.isVideo() || attachment.isSpoiler()) continue;
            attachment.retrieveInputStream().thenAccept(stream -> {
//                System.out.println(Main.encodeStreamToBase64(stream));
                Paste file = new Paste(
                        "Content By "+e.getAuthor().getAsTag()+" ("+e.getAuthor().getId()+")",
                        "Made with WriterBot. Join at https://discord.vectlabs.xyz",
                        "unlisted",
                        List.of(new PasteFile(attachment.getFileName(),
                                new PasteFileContent(
                                        "text",
                                        "null",
                                        Main.streamToString(stream)
                                )))
                );
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    client.sendAsync(
                            HttpRequest.newBuilder()
                                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(file)))
                                    .header("content-type", "application/json")
                                    .header("Authorization", "Key "+System.getenv("API_KEY"))
                                    .uri(uri.build())
                                    .build(),
                            HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
//                        System.out.println(response.body());
                        if (response.statusCode() == 201) {
                            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject().get("result").getAsJsonObject();
                            e.getMessage().reply(attachment.getFileName()
                                    + " by " + e.getAuthor().getAsMention() + ": https://paste.gg/" + json.get("id").getAsString()).queue();
                        }else {
                            e.getMessage().reply("Failed to create paste.").queue();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

}
