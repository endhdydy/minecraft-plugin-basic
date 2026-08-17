package com.example.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.entity.Player;
import java.util.*;

public class GuiManager {

    public static final String MENU_TITLE = "§6§l메인 메뉴";
    public static final String KIT_TITLE = "§b§l키트 선택";

    private final Map<UUID, String> openGuis = new HashMap<>();

    public Inventory createMainMenu() {
        Inventory inv = Bukkit.createInventory(null, 27, MENU_TITLE);

        // 키트
        inv.setItem(11, makeItem(Material.CHEST, "§b§l키트", "§7필요한 아이템을 받아보세요!", "§e클릭하여 열기"));

        // 텔레포트
        inv.setItem(13, makeItem(Material.COMPASS, "§a§l텔레포트", "§7월드 이동", "§e준비 중..."));

        // 정보
        inv.setItem(15, makeItem(Material.BOOK, "§6§l서버 정보", "§7플레이어 수: §e" + Bukkit.getOnlinePlayers().size()));

        // 장식
        fillBorder(inv, Material.GRAY_STAINED_GLASS_PANE);
        return inv;
    }

    public Inventory createKitMenu() {
        Inventory inv = Bukkit.createInventory(null, 27, KIT_TITLE);

        inv.setItem(10, makeItem(Material.DIAMOND_SWORD, "§b§l전사 키트", "§7다이아몬드 검, 방어구", "§e클릭하여 수령"));
        inv.setItem(12, makeItem(Material.DIAMOND_PICKAXE, "§e§l광부 키트", "§7곡괭이, 횃불, 음식", "§e클릭하여 수령"));
        inv.setItem(14, makeItem(Material.BOW, "§a§l궁수 키트", "§7활, 화살, 가죽 방어구", "§e클릭하여 수령"));
        inv.setItem(16, makeItem(Material.CAKE, "§d§l건축가 키트", "§7건축 블록 64개 x 4종", "§e클릭하여 수령"));

        fillBorder(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        return inv;
    }

    public void giveKit(Player player, String kitName) {
        Inventory inv = player.getInventory();
        switch (kitName.toLowerCase()) {
            case "전사" -> {
                inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
                inv.addItem(new ItemStack(Material.DIAMOND_HELMET));
                inv.addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
                inv.addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
                inv.addItem(new ItemStack(Material.DIAMOND_BOOTS));
                player.sendMessage("§a전사 키트를 지급받았습니다! ⚔️");
            }
            case "광부" -> {
                inv.addItem(new ItemStack(Material.DIAMOND_PICKAXE));
                inv.addItem(new ItemStack(Material.TORCH, 32));
                inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));
                player.sendMessage("§a광부 키트를 지급받았습니다! ⛏️");
            }
            case "궁수" -> {
                inv.addItem(new ItemStack(Material.BOW));
                inv.addItem(new ItemStack(Material.ARROW, 64));
                inv.addItem(new ItemStack(Material.LEATHER_HELMET));
                inv.addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
                inv.addItem(new ItemStack(Material.LEATHER_LEGGINGS));
                inv.addItem(new ItemStack(Material.LEATHER_BOOTS));
                player.sendMessage("§a궁수 키트를 지급받았습니다! 🏹");
            }
            case "건축가" -> {
                inv.addItem(new ItemStack(Material.STONE, 64));
                inv.addItem(new ItemStack(Material.OAK_PLANKS, 64));
                inv.addItem(new ItemStack(Material.GLASS, 64));
                inv.addItem(new ItemStack(Material.OAK_LOG, 64));
                player.sendMessage("§a건축가 키트를 지급받았습니다! 🏗️");
            }
        }
    }

    private ItemStack makeItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private void fillBorder(Inventory inv, Material borderMat) {
        ItemStack border = new ItemStack(borderMat);
        ItemMeta meta = border.getItemMeta();
        meta.setDisplayName(" ");
        border.setItemMeta(meta);
        for (int slot : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26}) {
            inv.setItem(slot, border);
        }
    }

    public void setOpenGui(UUID uuid, String title) { openGuis.put(uuid, title); }
    public String getOpenGui(UUID uuid) { return openGuis.get(uuid); }
    public void removeOpenGui(UUID uuid) { openGuis.remove(uuid); }
}