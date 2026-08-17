package com.example.database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class DbCommand implements CommandExecutor {

    private final Main plugin;
    private final Database db;
    private final PlaytimeTracker tracker;

    public DbCommand(Main plugin, Database db, PlaytimeTracker tracker) {
        this.plugin = plugin;
        this.db = db;
        this.tracker = tracker;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능합니다.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("playtime")) {
            return showPlaytime(player);
        }

        if (args.length == 0) {
            player.sendMessage("§6=== DatabasePlugin ===");
            player.sendMessage("§e/playtime §7- 플레이타임 확인");
            player.sendMessage("§e/db stats §7- 내 통계 확인");
            player.sendMessage("§e/db top §7- 플레이타임 순위");
            player.sendMessage("§e/db reset §7- 내 통계 초기화");
            return true;
        }

        return switch (args[0].toLowerCase()) {
            case "stats" -> showStats(player);
            case "top" -> showTop(player);
            case "reset" -> resetStats(player);
            default -> {
                player.sendMessage("§c알 수 없는 명령어. /db");
                yield true;
            }
        };
    }

    private boolean showPlaytime(Player player) {
        try {
            var stats = db.getStats(player.getUniqueId().toString());
            if (stats == null) {
                player.sendMessage("§c아직 기록이 없습니다.");
            } else {
                player.sendMessage("§6=== 플레이타임 ===");
                player.sendMessage("§e" + stats.getPlaytimeFormatted());
            }
        } catch (SQLException e) {
            player.sendMessage("§c오류: " + e.getMessage());
        }
        return true;
    }

    private boolean showStats(Player player) {
        try {
            var stats = db.getStats(player.getUniqueId().toString());
            if (stats == null) {
                player.sendMessage("§c아직 기록이 없습니다.");
                return true;
            }
            player.sendMessage("§6=== " + player.getName() + " 님의 통계 ===");
            player.sendMessage("§e플레이타임: §f" + stats.getPlaytimeFormatted());
            player.sendMessage("§e킬: §f" + stats.kills() + "  §6데스: §f" + stats.deaths());
            player.sendMessage("§eK/D: §f" + stats.getKdRatio());
            player.sendMessage("§e부순 블록: §f" + stats.blocksBroken() + "  §e설치한 블록: §f" + stats.blocksPlaced());
        } catch (SQLException e) {
            player.sendMessage("§c오류: " + e.getMessage());
        }
        return true;
    }

    private boolean showTop(Player player) {
        player.sendMessage("§6=== 플레이타임 TOP 10 ===");
        player.sendMessage("§7(준비 중)");
        return true;
    }

    private boolean resetStats(Player player) {
        player.sendMessage("§c통계 초기화는 관리자에게 문의하세요.");
        return true;
    }
}