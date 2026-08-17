package com.example.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandHandler implements CommandExecutor, Listener {

    private final Main plugin;
    private final Map<UUID, Boolean> godMode = new HashMap<>();

    public CommandHandler(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능한 명령어입니다.");
            return true;
        }

        return switch (command.getName().toLowerCase()) {
            case "gm" -> handleGameMode(player, args);
            case "fly" -> handleFly(player);
            case "heal" -> handleHeal(player, args);
            case "feed" -> handleFeed(player, args);
            case "spawn" -> handleSpawn(player);
            case "god" -> handleGod(player);
            default -> false;
        };
    }

    private boolean handleGameMode(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§c/gm <0|1|2|3>");
            return true;
        }

        GameMode mode = switch (args[0]) {
            case "0" -> GameMode.SURVIVAL;
            case "1" -> GameMode.CREATIVE;
            case "2" -> GameMode.ADVENTURE;
            case "3" -> GameMode.SPECTATOR;
            default -> null;
        };

        if (mode == null) {
            player.sendMessage("§c올바른 모드: 0(서바이벌), 1(크리에이티브), 2(어드벤처), 3(관전)");
            return true;
        }

        player.setGameMode(mode);
        player.sendMessage("§a게임모드가 §e" + mode.name().toLowerCase() + " §a(으)로 변경되었습니다.");
        return true;
    }

    private boolean handleFly(Player player) {
        boolean canFly = !player.getAllowFlight();
        player.setAllowFlight(canFly);
        player.setFlying(canFly);
        player.sendMessage(canFly
                ? "§a비행 모드가 §e활성화§a되었습니다."
                : "§a비행 모드가 §e비활성화§a되었습니다.");
        return true;
    }

    private boolean handleHeal(Player player, String[] args) {
        Player target = args.length > 0 ? plugin.getServer().getPlayer(args[0]) : player;
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return true;
        }

        target.setHealth(target.getMaxHealth());
        target.setFoodLevel(20);
        target.setSaturation(10f);
        target.setFireTicks(0);
        target.sendMessage("§a회복되었습니다!");
        if (!target.equals(player)) {
            player.sendMessage("§a" + target.getName() + " 님을 회복시켰습니다.");
        }
        return true;
    }

    private boolean handleFeed(Player player, String[] args) {
        Player target = args.length > 0 ? plugin.getServer().getPlayer(args[0]) : player;
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return true;
        }

        target.setFoodLevel(20);
        target.setSaturation(10f);
        target.sendMessage("§a배불러요! 🍖");
        if (!target.equals(player)) {
            player.sendMessage("§a" + target.getName() + " 님의 배고픔을 채웠습니다.");
        }
        return true;
    }

    private boolean handleSpawn(Player player) {
        World world = player.getWorld();
        Location spawn = world.getSpawnLocation();
        player.teleport(spawn);
        player.sendMessage("§a스폰으로 이동했습니다.");
        return true;
    }

    private boolean handleGod(Player player) {
        UUID uuid = player.getUniqueId();
        boolean enabled = !godMode.getOrDefault(uuid, false);
        godMode.put(uuid, enabled);

        player.sendMessage(enabled
                ? "§c무적 모드 §a활성화§c!"
                : "§c무적 모드 §e비활성화§c!");
        return true;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && isGodMode(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    public boolean isGodMode(UUID uuid) {
        return godMode.getOrDefault(uuid, false);
    }
}