package com.example.advanced;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RecipeListener implements Listener {

    public RecipeListener(Main plugin) {}

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // 접속 시 커스텀 조합법 자동으로 표시 (Key 목록)
        java.util.List<org.bukkit.NamespacedKey> keys = java.util.List.of(
            new org.bukkit.NamespacedKey("advanced-plugin", "saddle"),
            new org.bukkit.NamespacedKey("advanced-plugin", "name_tag"),
            new org.bukkit.NamespacedKey("advanced-plugin", "grass_block"),
            new org.bukkit.NamespacedKey("advanced-plugin", "emerald_to_diamond")
        );
        event.getPlayer().discoverRecipes(keys);
    }
}