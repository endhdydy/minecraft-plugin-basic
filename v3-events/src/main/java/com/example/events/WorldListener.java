package com.example.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class WorldListener implements Listener {

    private final Main plugin;
    private final Map<String, Long> lastBreakLog = new HashMap<>();

    public WorldListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();

        // 다이아몬드 광물 로그
        if (block.getType() == Material.DIAMOND_ORE || block.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            plugin.getServer().broadcastMessage("§b◆ §f" + p.getName() + " 님이 다이아몬드를 캤습니다! ["
                    + block.getX() + ", " + block.getY() + ", " + block.getZ() + "]");
        }

        // 연속 채굴 로그 방지
        String key = p.getName() + ":" + block.getWorld().getName();
        long now = System.currentTimeMillis();
        if (now - lastBreakLog.getOrDefault(key, 0L) > 1000) {
            lastBreakLog.put(key, now);
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        // 폭발로 인한 블록 파괴를 50%만 적용
        var blocks = event.blockList();
        if (!blocks.isEmpty()) {
            int toRemove = blocks.size() / 2;
            for (int i = 0; i < toRemove && !blocks.isEmpty(); i++) {
                blocks.remove(blocks.size() - 1 - i);
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        // 비가 오면 5초 후 맑게 (간단한 날씨 조절)
        if (event.toWeatherState()) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                event.getWorld().setStorm(false);
                event.getWorld().setThundering(false);
                event.getWorld().setWeatherDuration(0);
            }, 100L);
        }
    }
}