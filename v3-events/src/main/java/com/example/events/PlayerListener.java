package com.example.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {

    private final Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendTitle("§6§l환영합니다!", "§f" + player.getName() + " 님, 서버에 오신 걸 환영합니다!", 10, 60, 20);
        event.setJoinMessage("§a+ §f" + player.getName() + " 님이 접속하셨습니다.");

        // 환영 아이템 지급
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName("§6§l서버 가이드");
        compass.setItemMeta(meta);
        player.getInventory().addItem(compass);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§c- §f" + event.getPlayer().getName() + " 님이 퇴장하셨습니다.");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getServer().broadcastMessage("§c" + player.getName() + " 님이 사망했습니다! [좌표: "
                + player.getLocation().getBlockX() + ", "
                + player.getLocation().getBlockY() + ", "
                + player.getLocation().getBlockZ() + "]");
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("§a부활하셨습니다! /spawn 으로 스폰으로 이동하세요.");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            // 낙하 데미지 50% 감소
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setDamage(event.getDamage() * 0.5);
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            // 배고픔 감소 속도 70%로 완화
            int diff = event.getFoodLevel() - ((Player) event.getEntity()).getFoodLevel();
            if (diff < 0 && Math.random() > 0.3) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            event.getPlayer().sendMessage("§a좋은 꿈 꾸세요! 🌙");
        }
    }
}