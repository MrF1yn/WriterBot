package dev.mrflyn.writerbot;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.mrflyn.writerbot.apis.API;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStream;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 0-botToken
 * 1-pasteGGApiKey
 * 2-host
 * 3-database
 * 4-username
 * 5-password
 * 6-table
 */

public class Main {
    public static Bot bot;
    public static String[] envVariables;
    public static List<String> allAPIs;
    public static int port;
    public static void main(String[] args) throws URISyntaxException {
        allAPIs = new ArrayList<>();
        for(API a: API.values()){
            allAPIs.add(a.name());
        }
        try {
            envVariables = new String[7];
            envVariables[0] = System.getenv("DC_BOT_TOKEN");
            envVariables[1] = System.getenv("PASTEGG_API_KEY");
            envVariables[2] = System.getenv("DB_HOST");
            envVariables[3] = System.getenv("DB_DATABASE");
            envVariables[4] = System.getenv("DB_USERNAME");
            envVariables[5] = System.getenv("DB_PASSWORD");
            envVariables[6] = System.getenv("DB_TABLE");
            port = Integer.parseInt(System.getenv("DB_PORT"));
            for(String s : envVariables){
                if(s==null){
                    System.out.println("Error in env variables");
                    System.exit(0);
                }
            }
        }catch (Exception e){
            System.out.println("Error in env variables.");
            System.exit(0);
        }

        bot = new Bot();
        bot.enable();
//        AtomicBoolean b = new AtomicBoolean(true);
//        URIBuilder uri = new URIBuilder("https://paste.md-5.net/documents");
//        String s = "{\"name\":\"Content By MrF1yn#7119 (847383537497800714)\",\"description\":\"Made with WriterBot. Join at https://discord.vectlabs.xyz\",\"visibility\":\"unlisted\",\"files\":[{\"name\":\"message-5.txt\",\"content\":{\"format\":\"text\",\"highlight_language\":\"null\",\"value\":\"Sparky AntiCheat - Disclosure agreement \\r\\nCurrent TOS: Saturday, March 6, 2021 at 10:54:58pm GMT+2\\r\\nPlease pay attention when reading the Terms of Service (\\\"TOS\\\", \\\"Terms\\\") before you continue to the use of our Software (\\\"Service\\\", \\\"Product\\\", \\\"Copy\\\") operated by Sparky Technologies (\\\"us\\\", \\\"we\\\", \\\"our\\\", \\\"i\\\"). By accessing or using our Service, you agree to be bound by the TOS, if you do not agree with our TOS you are not permitted to use our Service and/or interact with our services abilities (\\\"detections\\\", \\\"support\\\", \\\"licensing\\\").\\r\\nTerms legally defined:\\r\\nTerm 1). You (\\\"the customer\\\", \\\"the affiliate\\\", \\\"the reader\\\") must be of legal age (18+) in your country or state region or have permission from your parent/guardian to purchase and/or use this Service. \\r\\nTerm 2). Attempting to receive your money in a form called \\\"Chargeback\\\" will result in an immediate termination of your access to Sparky AntiCheat 2.0. We do not offer \\\"refunds\\\" for this product unless told otherwise. \\r\\nTerm 3). Re-distributing (\\\"leaking\\\", \\\"sharing\\\", \\\"re-posting\\\", \\\"re-publishing\\\", \\\"spreading\\\") the service, will result in a termination of your access to the Service. \\r\\nTerm 4). De-obfuscating (\\\"un-hiding\\\", \\\"un-protecting\\\", \\\"dumping\\\", \\\"cracking\\\") the service, will result in a termination of your access to the Service. \\r\\nTerm 5). We are not liable for any damage caused by the act of attempting to re-distribute (\\\"leak\\\", \\\"share\\\", \\\"post\\\") your access to the service or access the obfuscated (\\\"hidden\\\", \\\"protected\\\", \\\"private\\\") code of the service, nor are we liable for the repercussions that may happen due to you leaking your access to the service. \\r\\nTerm 6). We are not liable for any damage caused by the attempted use of a re-distributed (\\\"leaked\\\", \\\"shared\\\", \\\"posted\\\") access to the service or de-obfuscated (\\\"un-hidden\\\", \\\"un-protected\\\", \\\"dumped\\\") code and/or jar(s) (\\\"loader\\\", \\\"dumped jar\\u0027) nor are we liable for the repercussions that may happen due to you using a leaked version of the service. \\r\\nTerm 7). We reserve the right to modify these Terms of Service indefinitely without announcement and without re-agreement, you (\\\"the customer\\\", \\\"the affiliate\\\", \\\"the reader\\u0027) are held liable for keeping up to date with our terms/rules\\r\\nTerm 8). No refunds once you have passed the allotted time given to you by the \\\"5.A Refund Policy\\\".\\r\\nTerm 9). You agree to this by starting (\\\"launching\\\", \\\"loading\\\", \\\"enabling\\\", \\\"running\\\", \\\"downloading\\\") a copy of any of our digital products (\\\"Sparky\\\", \\\"Sparky AntiCheat\\\", \\\"AntiCheat Software\\\") or by posting a message inside our private (\\\"buyer only\\\", \\\"customer only\\\", \\\"affiliate only\\\") discord server.\\r\\nSpecific(s):\\r\\n1.A Termination - We reserve the right to terminate (\\\"kill\\\", \\\"end\\\", \\\"suspend\\\") your access to our service immediately, without prior notice or liability, for any reason, including without limitation if you breach the TOS.\\r\\n1.B Indemnity Clause - You (\\\"the customer\\\", \\\"the affiliate\\\", \\\"the reader\\\") agree that you will be responsible for your use of the software, website, discord, and any other product/project you might obtain that is related to or affiliated with Sparky AntiCheat, and you agree to defend, indemnify, and hold harmless Sparky AntiCheat and its directors, affiliates and agents from and against any and all claims, liabilities, damages, losses, and expenses arising out of or in any way connected with your access to, use of, or alleged use of the software, website, discord, and any other product/project you might obtain that is related to or affiliated with Sparky AntiCheat.\\r\\n2.A Ownership - You (\\\"the paying customer\\\", \\\"the customer\\\") are held accountable as the legal owner (\\\"the legal administrator\\\") of your access to Sparky AntiCheat and due to that if any malicious actions occur to your copy of the service you will be held accountable and punished based off the actions that occurred. Redistributing the service is not allowed and is required that you contact an official Sparky admin using the buyers only discord to verify that you are allowed to redistribute to any person(s).\\r\\n2.B Privacy - We choose to reserve the right to keep our intellectual property (\\\"design(s)\\\", \\\"code\\\", \\\"service\\\") hidden behind Java Obfuscation and therefore take action upon any interaction with the Obfuscated code (of our intellectual property) that we (\\\"the administration\\\") view as malicious. If said incident is to occur the administration that holds rights to Sparky AntiCheat will at that point decide the fate (\\\"outcome\\\") of said user\\u0027s punishment for their actions. \\r\\n3.A User protection agreement - You (\\\"the customer\\\", \\\"the affiliate\\\", \\\"the reader\\\") are not allowed to release (\\\"post\\\", \\\"share\\\", \\\"leak\\\", \\\"give out\\\", \\\"publish\\\", \\\"spread\\\") any information posted into the official Sparky AntiCheat discord and/or any user (\\\"customer\\\", \\\"reader\\\", \\\"affiliate\\\") that is in this discord. All users (\\\"customers\\\", \\\"readers\\\", \\\"affiliates\\\") in this discord are requiring that their information related to them being in or involved with this discord and/or service to be private. If you decide to leak information like this we will terminate your access to the service.\\r\\n4.A Data Collection - We collect information such as starts (server startup), Backend IP(s), verifications per server, bans, player count, total violations, CPU Usuage, RAM Usuage, any and all error(s) log(s) related to our software, along with any and all actions taken in the form of an interaction between any party and our Software. We have decided to collect the logs and connection times of specific users (\\\"persons\\\", \\\"players\\\") due to tendencies that they (\\\"persons\\\", \\\"players\\\") have. We also collect any and all attempts to dump (\\\"De-obfuscate\\\") our service, and/or failure to load the plugin (\\\"service\\\") without the key (\\\"License\\\") and the attempted use of a disabled (\\\"killed\\\", \\\"terminated\\\", \\\"ended\\\", \\\"suspended\\\") copy (\\\"service\\\".)\\r\\n4.B Sparky App - We have designated an discord application (\\\"discord app\\\", \\\"discord bot\\\") to automate the process of authenticating our license system. This discord app is not allowed to be modified, investigated (\\\"deobfuscated\\\", \\\"debugged\\\", \\\"reverse engineered\\\"), or redistributed to any person(s) according to the \\\"Ownership\\\" term in our TOS. This app uses portions of your servers machine data to provide identifiable information to allow us to automate our licensing system.\\r\\n5.A Refund Policy - We have a 2 day refund policy for any issues that might happen making Sparky incompatible with your server.\"}}]}\n";
//        String j = "public Paste(String name, String description, String visibility, List<PasteFile> files){\n" +
//                "        this.name = name;\n" +
//                "        this.description = description;\n" +
//                "        this.visibility = visibility;\n" +
//                "//        this.expires = expires;\n" +
//                "        this.files = files;\n" +
//                "    }";
//        HttpClient client = HttpClient.newHttpClient();
//        client.sendAsync(
//                HttpRequest.newBuilder()
//                        .POST(HttpRequest.BodyPublishers.ofString(j, StandardCharsets.UTF_8))
//                        .header("content-type", "text/plain")
////                        .header("Authorization", "Key "+System.getenv("API_KEY"))
//                        .uri(uri.build())
//                        .build(),
//                HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
//                        System.out.println(response.body());
//                        System.out.println(response.statusCode());
////            if (response.statusCode() == 201) {
////                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject().get("result").getAsJsonObject();
////                System.out.println(json.get("id").getAsString());
////            }else {
////                System.out.println("Debug-response-json: "+response.body());
////            }
//            b.set(false);
//        });
//        while (b.get()){
//
//        }
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
