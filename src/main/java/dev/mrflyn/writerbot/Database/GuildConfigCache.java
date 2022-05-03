package dev.mrflyn.writerbot.Database;
import dev.mrflyn.writerbot.Bot;
import dev.mrflyn.writerbot.Main;
import dev.mrflyn.writerbot.apis.API;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.ConcurrentHashMap;

public class GuildConfigCache {
    public static ConcurrentHashMap<Long, GuildConfigCache> loadedCaches = new ConcurrentHashMap<>();

    public static void onGuildJoin(long id){
        if(loadedCaches.containsKey(id))return;
        Main.bot.database.createGuild(id);
        loadedCaches.put(id, new GuildConfigCache(id));
    }

    public static void loadAllCaches(){
        loadedCaches.clear();
        for(Guild guild : Bot.jda.getGuilds()){
            long id = guild.getIdLong();
            GuildConfigCache cache = Main.bot.database.getGuildIfExists(id);
            if(cache==null){
                Main.bot.database.createGuild(id);
                loadedCaches.put(id, new GuildConfigCache(id));
                continue;
            }
            loadedCaches.put(id, cache);
        }
    }

    private Long guildId;
    private API activatedApi;
    private boolean autoDelete;
    private boolean failSilently;
    private boolean changed;

    public GuildConfigCache(Long guildId, API activatedApi, boolean autoDelete, boolean failSilently){
        this.activatedApi = activatedApi;
        this.guildId = guildId;
        this.autoDelete = autoDelete;
        this.failSilently = failSilently;
        changed = false;
    }

    public GuildConfigCache(Long guildId) {
        this.activatedApi = API.HELP_CHAT;
        this.guildId = guildId;
        this.autoDelete = false;
        this.failSilently = false;
        changed = true;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public API getActivatedApi() {
        return activatedApi;
    }

    public void setActivatedApi(API activatedApi) {
        this.activatedApi = activatedApi;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public boolean isFailSilently() {
        return failSilently;
    }

    public void setFailSilently(boolean failSilently) {
        this.failSilently = failSilently;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
