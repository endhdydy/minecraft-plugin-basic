package com.example.basic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelloCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage("§6[BasicPlugin] §f안녕하세요, §e" + player.getName() + "§f님!");
        } else {
            sender.sendMessage("[BasicPlugin] 콘솔에서도 실행 가능합니다!");
        }
        return true;
    }
}