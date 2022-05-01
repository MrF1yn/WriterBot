package dev.mrflyn.writerbot;

import dev.mrflyn.writerbot.apis.API;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static dev.mrflyn.writerbot.Main.bot;

public class SpigotMain extends JavaPlugin {
    public static SpigotMain plugin;
    @Override
    public void onEnable(){
        plugin = this;
        BukkitLibraryManager libraryManager = new BukkitLibraryManager(plugin);

        Library jda = Library.builder()
                .groupId("net{}dv8tion") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("JDA")
                .version("5.0.0-alpha.10")
                // The following are optional

                // Sets an id for the library
                .id("jda")
                // Relocation is applied to the downloaded jar before loading it
                .relocate("net{}dv8tion{}jda", "writerbot{}net{}dv8tion{}jda") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                // The library is loaded into an IsolatedClassLoader, which is in common between every library with the same id
                .isolatedLoad(true)
                .build();
        Library mysql = Library.builder()
                .groupId("mysql") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mysql-connector-java")
                .version("8.0.29")
                // The following are optional

                // Sets an id for the library
                .id("mysql")
                // Relocation is applied to the downloaded jar before loading it
                .relocate("com{}mysql", "writerbot{}com{}mysql") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                // The library is loaded into an IsolatedClassLoader, which is in common between every library with the same id
                .isolatedLoad(true)
                .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(jda);
        libraryManager.loadLibrary(mysql);
        saveDefaultConfig();
        Main.allAPIs = new ArrayList<>();
        for(API a: API.values()){
            Main.allAPIs.add(a.name());
        }
        try {
            Main.envVariables = new String[7];
            Main.envVariables[0] = getConfig().getString("DC_BOT_TOKEN");
            Main.envVariables[1] = getConfig().getString("PASTEGG_API_KEY");
            Main.envVariables[2] = getConfig().getString("DB_HOST");
            Main.envVariables[3] = getConfig().getString("DB_DATABASE");
            Main.envVariables[4] = getConfig().getString("DB_USERNAME");
            Main.envVariables[5] = getConfig().getString("DB_PASSWORD");
            Main.envVariables[6] = getConfig().getString("DB_TABLE");
            Main.port = Integer.parseInt(getConfig().getString("DB_PORT"));
            for(String s : Main.envVariables){
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
    }

}
