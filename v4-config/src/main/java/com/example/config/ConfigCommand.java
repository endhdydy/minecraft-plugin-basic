package com.example.config;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public ConfigCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능합니다.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("setspawn")) {
            Location loc = player.getLocation();
            plugin.getConfigManager().saveSpawnLocation(loc);
            player.sendMessage("§a스폰 위치가 설정되었습니다! (§e"
                    + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "§a)");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§6=== ConfigPlugin 도움말 ===");
            player.sendMessage("§e/cp reload §7- 설정 리로드");
            player.sendMessage("§e/cp get §7- 설정값 보기");
            player.sendMessage("§e/setspawn §7- 현재 위치를 스폰으로 설정");
            return true;
        }

        return switch (args[0].toLowerCase()) {
            case "reload" -> {
                plugin.getConfigManager().load();
                player.sendMessage(plugin.getConfigManager().getWelcomeMessage());
                yield true;
            }
            case "get" -> {
                var mgr = plugin.getConfigManager();
                player.sendMessage("§6=== 현재 설정 ===");
                player.sendMessage("§e낙하 데미지: §f" + (mgr.getFallDamageMultiplier() * 100) + "%");
                player.sendMessage("§e배고픔 감소: §f" + (mgr.getHungerDrainMultiplier() * 100) + "%");
                player.sendMessage("§e폭발 블록 보호: §f" + (mgr.isPreventExplosionGriefing() ? "ON" : "OFF"));
                player.sendMessage("§e불 확산 방지: §f" + (mgr.isPreventFireSpread() ? "ON" : "OFF"));
                yield true;
            }
            default -> {
                player.sendMessage("§c알 수 없는 명령어입니다. /cp");
                yield true;
            }
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                 @NotNull Command command,
                                                 @NotNull String label,
                                                 @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload", "get");
        }
        return List.of();
    }
}