package dev.mrflyn.writerbot.apis;

import com.google.gson.JsonParser;
import dev.mrflyn.writerbot.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.http.client.utils.URIBuilder;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HelpChat implements ApiInterface{
    private URIBuilder uri = null;
    public HelpChat() {
        try {
            uri = new URIBuilder("https://paste.helpch.at/documents");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public HttpRequest post(InputStream stream, String attachmentName, User author) throws URISyntaxException {
                return HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "//Content By " + author.getAsTag() + " (" + author.getId() + ")\r\n"+
                                "//Made with WriterBot. Join at https://discord.vectlabs.xyz\r\n"+
                                Main.streamToString(stream), StandardCharsets.UTF_8))
                        .header("content-type", "text/plain")
                        .uri(uri.build())
                        .build();
    }

    @Override
    public int success() {
        return 200;
    }

    @Override
    public String getLink(HttpResponse<String> response) {
        if(response.statusCode()!=success())return null;
        String key = JsonParser.parseString(response.body()).getAsJsonObject().get("key").getAsString();
        return  "https://paste.helpch.at/" + key;
    }

    @Override
    public API getInstance() {
        return API.HELP_CHAT;
    }
}
