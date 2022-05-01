package dev.mrflyn.writerbot.Database;
import dev.mrflyn.writerbot.Main;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class Updater extends TimerTask {
    ReentrantLock lock = new ReentrantLock();
    @Override
    public void run(){
        lock.lock();
        for(GuildConfigCache cache : GuildConfigCache.loadedCaches.values()){
            Main.bot.database.update(cache);
        }
        lock.unlock();
        System.out.println("Database updated.");
    }
}
