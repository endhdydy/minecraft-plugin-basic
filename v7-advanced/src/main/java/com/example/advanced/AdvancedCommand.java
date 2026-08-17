package com.example.advanced;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdvancedCommand implements CommandExecutor {

    private final Main plugin;

    public AdvancedCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        return switch (command.getName().toLowerCase()) {
            case "warp" -> handleWarp(sender, args);
            case "warpadmin" -> handleWarpAdmin(sender, args);
            case "combatlog" -> handleCombatLog(sender);
            case "customcraft" -> handleCustomCraft(sender);
            case "mute" -> handleMute(sender, args);
            case "unmute" -> handleUnmute(sender, args);
            case "msg" -> handleMsg(sender, args);
            default -> false;
        };
    }

    private boolean handleWarp(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능합니다.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage("§6=== 워프 목록 ===");
            for (String name : plugin.getWarpManager().list()) {
                player.sendMessage("§e- " + name);
            }
            return true;
        }

        // 전투 상태 체크
        if (plugin.getCombatManager().isInCombat(player)) {
            int sec = plugin.getCombatManager().getRemainingSeconds(player);
            player.sendMessage("§c전투 중입니다! " + sec + "초 후에 사용 가능합니다.");
            return true;
        }

        if (plugin.getWarpManager().teleport(args[0], player)) {
            player.sendMessage("§a'" + args[0] + "' (으)로 이동했습니다.");
        } else {
            player.sendMessage("§c존재하지 않는 워프입니다.");
        }
        return true;
    }

    private boolean handleWarpAdmin(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능합니다.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage("§c/warpadmin <create|remove|list> [name]");
            return true;
        }
        return switch (args[0].toLowerCase()) {
            case "create" -> {
                if (args.length < 2) { player.sendMessage("§c/warpadmin create <name>"); yield true; }
                if (plugin.getWarpManager().create(args[1], player)) {
                    player.sendMessage("§a워프 '" + args[1] + "' 생성됨!");
                } else {
                    player.sendMessage("§c이미 존재하는 워프입니다.");
                }
                yield true;
            }
            case "remove" -> {
                if (args.length < 2) { player.sendMessage("§c/warpadmin remove <name>"); yield true; }
                if (plugin.getWarpManager().remove(args[1])) {
                    player.sendMessage("§a워프 '" + args[1] + "' 삭제됨!");
                } else {
                    player.sendMessage("§c존재하지 않는 워프입니다.");
                }
                yield true;
            }
            case "list" -> {
                player.sendMessage("§6=== 워프 목록 ===");
                for (String name : plugin.getWarpManager().list()) {
                    player.sendMessage("§e- " + name);
                }
                yield true;
            }
            default -> {
                player.sendMessage("§c알 수 없는 명령어.");
                yield true;
            }
        };
    }

    private boolean handleCombatLog(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능합니다.");
            return true;
        }
        if (plugin.getCombatManager().isInCombat(player)) {
            int sec = plugin.getCombatManager().getRemainingSeconds(player);
            player.sendMessage("§c⚔ 전투 중! " + sec + "초 남음");
        } else {
            player.sendMessage("§a전투 중이 아닙니다.");
        }
        return true;
    }

    private boolean handleCustomCraft(CommandSender sender) {
        sender.sendMessage("§6=== 커스텀 조합법 ===");
        sender.sendMessage("§e달걀 + 철괴 + 가죽 §7→ 말 안장");
        sender.sendMessage("§e실 x2 + 종이 §7→ 이름표");
        sender.sendMessage("§e흙 x4 + 밀 씨앗 §7→ 잔디 블록 x4");
        sender.sendMessage("§e에메랄드 x4 §7→ 다이아몬드");
        return true;
    }

    private boolean handleMute(CommandSender sender, String[] args) {
        if (args.length == 0) { sender.sendMessage("§c/mute <player>"); return true; }
        if (plugin.getMuteManager().toggleMute(args[0])) {
            sender.sendMessage("§a" + args[0] + " 님을 뮤트했습니다.");
        } else {
            sender.sendMessage("§a" + args[0] + " 님의 뮤트를 해제했습니다.");
        }
        return true;
    }

    private boolean handleUnmute(CommandSender sender, String[] args) {
        if (args.length == 0) { sender.sendMessage("§c/unmute <player>"); return true; }
        if (plugin.getMuteManager().forceUnmute(args[0])) {
            sender.sendMessage("§a" + args[0] + " 님의 뮤트를 해제했습니다.");
        } else {
            sender.sendMessage("§c" + args[0] + " 님은 뮤트 상태가 아닙니다.");
        }
        return true;
    }

    private boolean handleMsg(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능합니다.");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage("§c/msg <player> <message>");
            return true;
        }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return true;
        }
        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        player.sendMessage("§7[§6귓§7 → §f" + target.getName() + "§7] §f" + message);
        target.sendMessage("§7[§6귓§7 ← §f" + player.getName() + "§7] §f" + message);
        return true;
    }
}