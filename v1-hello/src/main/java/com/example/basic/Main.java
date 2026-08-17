package com.example.basic;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("BasicPlugin 이 활성화되었습니다!");
        getCommand("hello").setExecutor(new HelloCommand());
        getCommand("ping").setExecutor(new PingCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("BasicPlugin 이 비활성화되었습니다.");
    }
}