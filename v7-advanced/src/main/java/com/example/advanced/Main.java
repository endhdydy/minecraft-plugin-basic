package com.example.advanced;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private WarpManager warpManager;
    private MuteManager muteManager;
    private CombatManager combatManager;
    private ChatManager chatManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        warpManager = new WarpManager(this);
        muteManager = new MuteManager(this);
        combatManager = new CombatManager(this);
        chatManager = new ChatManager(this, muteManager);

        // 리스너 등록
        getServer().getPluginManager().registerEvents(combatManager, this);
        getServer().getPluginManager().registerEvents(chatManager, this);
        getServer().getPluginManager().registerEvents(new RecipeListener(this), this);

        // 명령어 등록
        AdvancedCommand cmd = new AdvancedCommand(this);
        getCommand("warp").setExecutor(cmd);
        getCommand("warpadmin").setExecutor(cmd);
        getCommand("combatlog").setExecutor(cmd);
        getCommand("customcraft").setExecutor(cmd);
        getCommand("mute").setExecutor(cmd);
        getCommand("unmute").setExecutor(cmd);
        getCommand("msg").setExecutor(cmd);

        // 커스텀 조합법
        CustomRecipes.register(this);

        getLogger().info("AdvancedPlugin 활성화! (MC 1.21)");
    }

    @Override
    public void onDisable() {
        warpManager.save();
        getLogger().info("AdvancedPlugin 비활성화.");
    }

    public static Main getInstance() { return instance; }
    public WarpManager getWarpManager() { return warpManager; }
    public MuteManager getMuteManager() { return muteManager; }
    public CombatManager getCombatManager() { return combatManager; }
    public ChatManager getChatManager() { return chatManager; }
}