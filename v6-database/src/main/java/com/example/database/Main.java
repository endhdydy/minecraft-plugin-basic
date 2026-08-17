package com.example.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    private Database db;
    private PlaytimeTracker playtimeTracker;

    @Override
    public void onEnable() {
        try {
            db = new Database(this);
            playtimeTracker = new PlaytimeTracker(this, db);

            DbCommand cmd = new DbCommand(this, db, playtimeTracker);
            getCommand("db").setExecutor(cmd);
            getCommand("playtime").setExecutor(cmd);

            getServer().getPluginManager().registerEvents(playtimeTracker, this);

            getLogger().info("DatabasePlugin 활성화! (MC 1.21, SQLite)");
        } catch (SQLException e) {
            getLogger().severe("DB 초기화 실패: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (playtimeTracker != null) playtimeTracker.saveAll();
        if (db != null) db.close();
        getLogger().info("DatabasePlugin 비활성화.");
    }

    public Database getDb() { return db; }
    public PlaytimeTracker getPlaytimeTracker() { return playtimeTracker; }
}