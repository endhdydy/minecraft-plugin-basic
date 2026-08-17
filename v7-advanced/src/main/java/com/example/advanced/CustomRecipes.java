package com.example.advanced;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class CustomRecipes {

    public static void register(Main plugin) {
        // 말 안장 조합법 (원래는 생성 불가)
        ShapedRecipe saddle = new ShapedRecipe(
            new NamespacedKey(plugin, "saddle"),
            new ItemStack(Material.SADDLE)
        );
        saddle.shape("LLL", "LIL", "LIL");
        saddle.setIngredient('L', Material.LEATHER);
        saddle.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(saddle);

        // 이름표 조합법
        ShapedRecipe nameTag = new ShapedRecipe(
            new NamespacedKey(plugin, "name_tag"),
            new ItemStack(Material.NAME_TAG)
        );
        nameTag.shape(" SS", " S ", "P  ");
        nameTag.setIngredient('S', Material.STRING);
        nameTag.setIngredient('P', Material.PAPER);
        plugin.getServer().addRecipe(nameTag);

        // 흙 → 잔디 블록 (4개)
        ShapelessRecipe grassBlock = new ShapelessRecipe(
            new NamespacedKey(plugin, "grass_block"),
            new ItemStack(Material.GRASS_BLOCK, 4)
        );
        grassBlock.addIngredient(4, Material.DIRT);
        grassBlock.addIngredient(Material.WHEAT_SEEDS);
        plugin.getServer().addRecipe(grassBlock);

        // 에메랄드 → 다이아몬드
        ShapelessRecipe diamond = new ShapelessRecipe(
            new NamespacedKey(plugin, "emerald_to_diamond"),
            new ItemStack(Material.DIAMOND)
        );
        diamond.addIngredient(4, Material.EMERALD);
        plugin.getServer().addRecipe(diamond);

        plugin.getLogger().info("4개의 커스텀 조합법 등록됨!");
    }
}