package dev.mrflyn.writerbot;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dev.mrflyn.writerbot.Database.GuildConfigCache;
import dev.mrflyn.writerbot.Database.Updater;
import dev.mrflyn.writerbot.apis.API;
import net.dv8tion.jda.api.MessageBuilder;
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
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Listeners extends ListenerAdapter {
//    URIBuilder uri = new URIBuilder("https://api.paste.gg/v1/pastes");
    URIBuilder uri = new URIBuilder("https://paste.helpch.at/documents");
    Gson gson = new Gson();
    Pattern codeBlock = Pattern.compile("```(.+?)```", Pattern.DOTALL);
    Pattern codeLang = Pattern.compile("```(.+?)\n");

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
                if(cache.getActivatedApi() != API.valueOf(option)){
                    cache.setActivatedApi(API.valueOf(option));
                    cache.setChanged(true);
                }
                event.reply("Successfully set Service to: "+option+".").queue();
                break;
            case "failsilently":
                boolean fs = event.getOptions().get(0).getAsBoolean();
                if(cache.isFailSilently() != fs){
                    cache.setFailSilently(fs);
                    cache.setChanged(true);
                }
                event.reply("Successfully set Fail-Silently to: "+fs+".").queue();
                break;
            case "autodelete":
                boolean ad = event.getOptions().get(0).getAsBoolean();
                if(cache.isAutoDelete() != ad){
                    cache.setAutoDelete(ad);
                    cache.setChanged(true);
                }
                event.reply("Successfully set Auto-Delete to: "+ad+".").queue();
                break;
            case "codeblockupload":
                boolean cdu = event.getOptions().get(0).getAsBoolean();
                if (cache.isCodeBlockUpload() != cdu) {
                    cache.setCodeBlockUpload(cdu);
                    cache.setChanged(true);
                }
                event.reply("Successfully set CodeBlock-Upload to: " + cdu + ".").queue();
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
        boolean delete = false;
        if(cache.isCodeBlockUpload()) {
//            System.out.println(e.getMessage().getContentRaw());
            String raw = e.getMessage().getContentRaw();
            Matcher matcher = codeBlock.matcher(raw);
            while (matcher.find()){
                String s = matcher.group();
//                System.out.println(s);
                Matcher langMatcher = codeLang.matcher(s);
                langMatcher.find();
                String tempLang = langMatcher.group().replace("```","").split(" ")[0];
//                System.out.println(tempLang);
                if(tempLang == null||tempLang.equals("")||tempLang.equals(" ")){
                    tempLang = "txt";
                }
                final String lang = tempLang;
                s = s.replace("```","");
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    client.sendAsync(cache.getActivatedApi().getWrapper().post(s, "CodeBlockSnippet."+lang, e.getAuthor()),
                            HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {

                        System.out.println(response.body());

                        if (response.statusCode() == cache.getActivatedApi().getWrapper().success()) {
                            e.getMessage().reply("CodeBlockSnippet."+lang
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
            }
        }

        for (Message.Attachment attachment : e.getMessage().getAttachments()) {
            if (attachment.isImage() || attachment.isVideo() || attachment.isSpoiler()|| attachment.getFileName().endsWith(".jar")
            || attachment.getFileName().endsWith(".exe"))
                continue;
            delete = true;
            attachment.retrieveInputStream().thenAccept(stream -> {
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    client.sendAsync(cache.getActivatedApi().getWrapper().post(stream, attachment.getFileName(), e.getAuthor()),
                            HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {

                        System.out.println(response.body());

                        if (response.statusCode() == cache.getActivatedApi().getWrapper().success()) {
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
        if(cache.isAutoDelete()&&delete) {
            e.getMessage().delete().queue();
        }
    }

}
