package com.example.gui;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        guiManager = new GuiManager();

        MenuCommand menuCmd = new MenuCommand(this);
        getCommand("menu").setExecutor(menuCmd);
        getCommand("kit").setExecutor(menuCmd);

        getServer().getPluginManager().registerEvents(new GuiListener(guiManager), this);
        getLogger().info("GuiPlugin 활성화! (MC 1.21)");
    }

    @Override
    public void onDisable() {
        getLogger().info("GuiPlugin 비활성화.");
    }

    public static Main getInstance() { return instance; }
    public GuiManager getGuiManager() { return guiManager; }
}