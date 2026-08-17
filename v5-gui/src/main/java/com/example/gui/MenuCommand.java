package com.example.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor {

    private final Main plugin;

    public MenuCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c플레이어만 사용 가능합니다.");
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "menu" -> {
                player.openInventory(plugin.getGuiManager().createMainMenu());
                plugin.getGuiManager().setOpenGui(player.getUniqueId(), GuiManager.MENU_TITLE);
                player.sendMessage("§a메뉴를 열었습니다.");
            }
            case "kit" -> {
                player.openInventory(plugin.getGuiManager().createKitMenu());
                plugin.getGuiManager().setOpenGui(player.getUniqueId(), GuiManager.KIT_TITLE);
            }
        }
        return true;
    }
}