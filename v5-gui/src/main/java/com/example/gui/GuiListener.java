package com.example.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class GuiListener implements Listener {

    private final GuiManager guiManager;

    public GuiListener(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().getTitle();

        if (!title.equals(GuiManager.MENU_TITLE) && !title.equals(GuiManager.KIT_TITLE)) return;
        event.setCancelled(true);

        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;
        String displayName = event.getCurrentItem().getItemMeta().getDisplayName();

        if (title.equals(GuiManager.MENU_TITLE)) {
            switch (displayName) {
                case "§b§l키트" -> {
                    player.openInventory(guiManager.createKitMenu());
                    guiManager.setOpenGui(player.getUniqueId(), GuiManager.KIT_TITLE);
                }
                case "§a§l텔레포트" -> player.sendMessage("§c텔레포트 기능은 준비 중입니다.");
                case "§6§l서버 정보" -> player.sendMessage("§6현재 접속자: §e" + player.getServer().getOnlinePlayers().size() + "명");
            }
        } else if (title.equals(GuiManager.KIT_TITLE)) {
            String kitName = switch (displayName) {
                case "§b§l전사 키트" -> "전사";
                case "§e§l광부 키트" -> "광부";
                case "§a§l궁수 키트" -> "궁수";
                case "§d§l건축가 키트" -> "건축가";
                default -> null;
            };
            if (kitName != null) {
                guiManager.giveKit(player, kitName);
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            guiManager.removeOpenGui(player.getUniqueId());
        }
    }
}