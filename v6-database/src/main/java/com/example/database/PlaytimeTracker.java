package com.example.database;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaytimeTracker implements Listener {

    private final JavaPlugin plugin;
    private final Database db;
    private final Map<UUID, Long> joinTimes = new HashMap<>();

    public PlaytimeTracker(JavaPlugin plugin, Database db) {
        this.plugin = plugin;
        this.db = db;

        // 30초마다 플레이타임 저장
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                savePlaytime(p);
            }
        }, 600L, 600L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        joinTimes.put(p.getUniqueId(), System.currentTimeMillis());
        try {
            db.upsertPlayer(p.getUniqueId().toString(), p.getName());
        } catch (SQLException e) {
            plugin.getLogger().warning("DB 저장 실패: " + e.getMessage());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        savePlaytime(event.getPlayer());
        joinTimes.remove(event.getPlayer().getUniqueId());
    }

    public void savePlaytime(Player player) {
        Long joinTime = joinTimes.get(player.getUniqueId());
        if (joinTime == null) return;
        long elapsed = (System.currentTimeMillis() - joinTime) / 1000;
        if (elapsed <= 0) return;
        try {
            db.addPlaytime(player.getUniqueId().toString(), elapsed);
        } catch (SQLException e) {
            plugin.getLogger().warning("플레이타임 저장 실패: " + e.getMessage());
        }
        joinTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void saveAll() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            savePlaytime(p);
        }
    }
}