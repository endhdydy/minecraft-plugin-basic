package com.example.basic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        long start = System.currentTimeMillis();
        sender.sendMessage("§e[BasicPlugin] §7Pong! 계산 중...");
        long latency = System.currentTimeMillis() - start;
        sender.sendMessage("§e[BasicPlugin] §a응답 시간: §f" + latency + "ms");
        return true;
    }
}