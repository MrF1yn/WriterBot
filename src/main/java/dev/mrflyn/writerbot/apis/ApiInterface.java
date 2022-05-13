package dev.mrflyn.writerbot.apis;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface ApiInterface {

    HttpRequest post(InputStream stream, String attachmentName, User author) throws URISyntaxException;
    HttpRequest post(String stream, String attachmentName, User author) throws URISyntaxException;

    int success();

    String getLink(HttpResponse<String> response);

    API getInstance();


}
