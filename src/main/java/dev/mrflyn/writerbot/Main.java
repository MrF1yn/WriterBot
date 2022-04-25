package dev.mrflyn.writerbot;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import dev.mrflyn.writerbot.pasteapi.Paste;
import dev.mrflyn.writerbot.pasteapi.PasteFile;
import dev.mrflyn.writerbot.pasteapi.PasteFileContent;
import org.apache.http.client.utils.URIBuilder;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        Bot bot = new Bot();
        bot.enable();
    }
    public static String encodeStreamToBase64(InputStream file) {
        try {
            byte[] fileContent = file.readAllBytes();
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }

    public static String streamToString(InputStream file) {
        try {
            byte[] fileContent = file.readAllBytes();
            return new String(fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }
}
