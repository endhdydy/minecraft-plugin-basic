package com.example.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CommandsPlugin 활성화!");

        commandHandler = new CommandHandler(this);
        getServer().getPluginManager().registerEvents(commandHandler, this);

        getCommand("gm").setExecutor(commandHandler);
        getCommand("fly").setExecutor(commandHandler);
        getCommand("heal").setExecutor(commandHandler);
        getCommand("feed").setExecutor(commandHandler);
        getCommand("spawn").setExecutor(commandHandler);
        getCommand("god").setExecutor(commandHandler);
    }

    @Override
    public void onDisable() {
        getLogger().info("CommandsPlugin 비활성화.");
    }

    public static Main getInstance() { return instance; }
    public CommandHandler getCommandHandler() { return commandHandler; }
}