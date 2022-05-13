package dev.mrflyn.writerbot.apis;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dev.mrflyn.writerbot.Main;
import dev.mrflyn.writerbot.apis.pasteggapi.Paste;
import dev.mrflyn.writerbot.apis.pasteggapi.PasteFile;
import dev.mrflyn.writerbot.apis.pasteggapi.PasteFileContent;
import net.dv8tion.jda.api.entities.User;
import org.apache.http.client.utils.URIBuilder;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PasteGG implements ApiInterface{
    private URIBuilder uri = null;
    Gson gson = new Gson();
    public PasteGG() {
        try {
            uri = new URIBuilder("https://api.paste.gg/v1/pastes");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public HttpRequest post(InputStream stream, String attachmentName, User author) throws URISyntaxException {
        Paste file = new Paste(
                "Content By " + author.getAsTag() + " (" + author.getId() + ")",
                "Made with WriterBot. Join at https://discord.vectlabs.xyz",
                "unlisted",
                List.of(new PasteFile(attachmentName,
                        new PasteFileContent(
                                "text",
                                "null",
                                Main.streamToString(stream)
                        )))
        );
        System.out.println(gson.toJson(file));
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(file).toString(), StandardCharsets.UTF_8))
                .header("content-type", "application/json")
                .header("Authorization", "Key "+Main.envVariables[1])
                .uri(uri.build())
                .build();
    }

    @Override
    public HttpRequest post(String stream, String attachmentName, User author) throws URISyntaxException {
        Paste file = new Paste(
                "Content By " + author.getAsTag() + " (" + author.getId() + ")",
                "Made with WriterBot. Join at https://discord.vectlabs.xyz",
                "unlisted",
                List.of(new PasteFile(attachmentName,
                        new PasteFileContent(
                                "text",
                                "null",
                                stream
                        )))
        );
        System.out.println(gson.toJson(file));
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(file).toString(), StandardCharsets.UTF_8))
                .header("content-type", "application/json")
                .header("Authorization", "Key " + Main.envVariables[1])
                .uri(uri.build())
                .build();
    }

    @Override
    public int success() {
        return 201;
    }

    @Override
    public String getLink(HttpResponse<String> response) {
        if(response.statusCode()!=success())return null;
        String key = JsonParser.parseString(response.body()).getAsJsonObject().get("result").getAsJsonObject().get("id").getAsString();
        return  "https://paste.gg/" + key;
    }

    @Override
    public API getInstance() {
        return API.PASTE_GG;
    }
}
