package com.example.advanced;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MuteManager {

    private final Main plugin;
    private final Set<UUID> muted = new HashSet<>();

    public MuteManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean isMuted(UUID uuid) {
        return muted.contains(uuid);
    }

    public boolean toggleMute(String name) {
        var player = plugin.getServer().getPlayer(name);
        if (player == null) return false;
        UUID uuid = player.getUniqueId();
        if (muted.contains(uuid)) {
            muted.remove(uuid);
            return false; // 해제
        } else {
            muted.add(uuid);
            return true; // 뮤트
        }
    }

    public boolean forceUnmute(String name) {
        var player = plugin.getServer().getPlayer(name);
        if (player == null) return false;
        return muted.remove(player.getUniqueId());
    }
}