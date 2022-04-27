package dev.mrflyn.writerbot;



import java.io.IOException;
import java.io.InputStream;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import java.util.Base64;


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
