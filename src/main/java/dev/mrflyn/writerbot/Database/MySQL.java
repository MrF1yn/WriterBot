package dev.mrflyn.writerbot.Database;

import dev.mrflyn.writerbot.Main;
import dev.mrflyn.writerbot.apis.API;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    private Connection connection;
    public String host, database, username, password, table;
    public int port;

    public void init() {
        host = Main.envVariables[2];
        port = Main.port;
        database = Main.envVariables[3];
        username = Main.envVariables[4];
        password = Main.envVariables[5];
        table = Main.envVariables[6];
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true&characterEncoding=utf8&useUnicode=true&interactiveClient=true", username, password);
                Statement statement = getConnection().createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.table+" (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "GUILD_ID BIGINT(255), ACTIVATED_API varchar(200) DEFAULT 'HELP_CHAT', AUTO_DELETE BOOLEAN DEFAULT false, " +
                        "FAIL_SILENTLY BOOLEAN DEFAULT false)");

                statement = getConnection().createStatement();
                statement.executeUpdate("ALTER TABLE "+this.table+" ADD COLUMN CODEBLOCK_UPLOAD BOOLEAN DEFAULT false");
                System.out.println("database connection successful.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean guildExists(long id) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM " + this.table + " WHERE GUILD_ID=?");
            statement.setLong(1, id);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                //found
                return true;
            }
            //not found

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createGuild(long id) {
        try {
            if (!guildExists(id)) {
                PreparedStatement insert = getConnection()
                        .prepareStatement("INSERT INTO " + this.table + " (GUILD_ID) VALUES (?)");
                insert.setLong(1, id);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(GuildConfigCache cache) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("UPDATE " + this.table + " SET ACTIVATED_API=?,AUTO_DELETE=?,FAIL_SILENTLY=?,CODEBLOCK_UPLOAD=? WHERE GUILD_ID=?");
            statement.setString(1, cache.getActivatedApi().name());
            statement.setBoolean(2, cache.isAutoDelete());
            statement.setBoolean(3, cache.isFailSilently());
            statement.setBoolean(4, cache.isCodeBlockUpload());
            statement.setLong(5, cache.getGuildId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public GuildConfigCache getGuildIfExists(long id) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM " + this.table + " WHERE GUILD_ID=?");
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();
            if(results.next()) {
                GuildConfigCache cache = new GuildConfigCache(
                        id,
                        API.valueOf(results.getString("ACTIVATED_API")),
                        results.getBoolean("AUTO_DELETE"),
                        results.getBoolean("FAIL_SILENTLY"),
                        results.getBoolean("CODEBLOCK_UPLOAD")
                );
                return cache;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}