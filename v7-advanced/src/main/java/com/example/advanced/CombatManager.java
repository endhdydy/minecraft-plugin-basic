package com.example.advanced;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class CombatManager implements Listener {

    private final Main plugin;
    private final Map<UUID, Long> combatTagged = new HashMap<>();
    private final long COMBAT_TAG_DURATION = 30_000; // 30초

    public CombatManager(Main plugin) {
        this.plugin = plugin;

        // 1초마다 전투 태그 확인
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            combatTagged.entrySet().removeIf(e -> now - e.getValue() > COMBAT_TAG_DURATION);
        }, 20L, 20L);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
            long now = System.currentTimeMillis();
            combatTagged.put(victim.getUniqueId(), now);
            combatTagged.put(attacker.getUniqueId(), now);
            victim.sendMessage("§c⚔ 전투 중! 30초간 /warp, /spawn 사용 불가!");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (isInCombat(event.getPlayer())) {
            event.getPlayer().setHealth(0);
            plugin.getServer().broadcastMessage("§c" + event.getPlayer().getName() + " 님이 전투 중 도망갔습니다! 사망!");
        }
    }

    public boolean isInCombat(Player player) {
        return combatTagged.containsKey(player.getUniqueId());
    }

    public int getRemainingSeconds(Player player) {
        Long time = combatTagged.get(player.getUniqueId());
        if (time == null) return 0;
        int remaining = (int) ((COMBAT_TAG_DURATION - (System.currentTimeMillis() - time)) / 1000);
        return Math.max(0, remaining);
    }
}