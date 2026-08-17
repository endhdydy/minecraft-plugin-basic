package com.example.advanced;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {

    private final Main plugin;
    private final MuteManager muteManager;

    public ChatManager(Main plugin, MuteManager muteManager) {
        this.plugin = plugin;
        this.muteManager = muteManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (muteManager.isMuted(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c현재 뮤트 상태입니다.");
            return;
        }

        // 채팅 포맷 변경
        event.setFormat("§7<§f" + event.getPlayer().getName() + "§7> §f" + event.getMessage());
    }
}