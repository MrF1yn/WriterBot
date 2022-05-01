package dev.mrflyn.writerbot;

import dev.mrflyn.writerbot.Database.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {
    /**
     * /activatedApi (apiName)
     * /failSilently (boolean)
     * /autoDelete (boolean)
     */
    public static JDA jda;
    public MySQL database;
    //called on program start
    public void enable(){

        database = new MySQL();
        database.init();
        try {
        jda = JDABuilder.createLight(Main.envVariables[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new Listeners())
                .setActivity(Activity.playing("paste"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
            jda.awaitReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
