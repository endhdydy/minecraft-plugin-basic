package com.example.config;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        configManager = new ConfigManager(this);
        configManager.load();

        ConfigCommand cmd = new ConfigCommand(this);
        getCommand("configplugin").setExecutor(cmd);
        getCommand("configplugin").setTabCompleter(cmd);
        getCommand("setspawn").setExecutor(cmd);

        getLogger().info("ConfigPlugin 활성화! (MC 1.21)");
    }

    @Override
    public void onDisable() {
        getLogger().info("ConfigPlugin 비활성화.");
    }

    public static Main getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
}