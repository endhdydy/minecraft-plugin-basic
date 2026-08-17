package com.example.config;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final Main plugin;
    private FileConfiguration config;

    // 설정값 캐시
    private String welcomeMessage;
    private String joinTitle;
    private String joinSubtitle;
    private double fallDamageMultiplier;
    private double hungerDrainMultiplier;
    private double explosionBlockDestruction;
    private boolean disableWeather;
    private boolean spawnEnabled;
    private Location spawnLocation;
    private boolean preventExplosionGriefing;
    private boolean preventFireSpread;
    private boolean preventLeafDecay;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.reloadConfig();
        config = plugin.getConfig();

        welcomeMessage = color(config.getString("welcome-message", "&a&l서버에 오신 걸 환영합니다!"));
        joinTitle = color(config.getString("join-title", "&6&l환영합니다!"));
        joinSubtitle = color(config.getString("join-subtitle", "&f즐거운 게임 되세요!"));

        fallDamageMultiplier = config.getDouble("gameplay.fall-damage-multiplier", 0.5);
        hungerDrainMultiplier = config.getDouble("gameplay.hunger-drain-multiplier", 0.7);
        explosionBlockDestruction = config.getDouble("gameplay.explosion-block-destruction", 0.5);
        disableWeather = config.getBoolean("gameplay.disable-weather", false);

        spawnEnabled = config.getBoolean("spawn.enabled", true);
        String worldName = config.getString("spawn.world", "world");
        World world = plugin.getServer().getWorld(worldName);
        if (world != null && spawnEnabled) {
            spawnLocation = new Location(
                world,
                config.getDouble("spawn.x", 0),
                config.getDouble("spawn.y", 64),
                config.getDouble("spawn.z", 0),
                (float) config.getDouble("spawn.yaw", 0),
                (float) config.getDouble("spawn.pitch", 0)
            );
        }

        preventExplosionGriefing = config.getBoolean("protection.prevent-explosion-griefing", true);
        preventFireSpread = config.getBoolean("protection.prevent-fire-spread", true);
        preventLeafDecay = config.getBoolean("protection.prevent-leaf-decay", false);

        plugin.getLogger().info("설정 로드 완료!");
    }

    public void saveSpawnLocation(Location loc) {
        config.set("spawn.world", loc.getWorld().getName());
        config.set("spawn.x", loc.getX());
        config.set("spawn.y", loc.getY());
        config.set("spawn.z", loc.getZ());
        config.set("spawn.yaw", (double) loc.getYaw());
        config.set("spawn.pitch", (double) loc.getPitch());
        plugin.saveConfig();
        load(); // 캐시 갱신
    }

    private String color(String text) {
        return text.replace('&', '§');
    }

    public String getWelcomeMessage() { return welcomeMessage; }
    public String getJoinTitle() { return joinTitle; }
    public String getJoinSubtitle() { return joinSubtitle; }
    public double getFallDamageMultiplier() { return fallDamageMultiplier; }
    public double getHungerDrainMultiplier() { return hungerDrainMultiplier; }
    public double getExplosionBlockDestruction() { return explosionBlockDestruction; }
    public boolean isDisableWeather() { return disableWeather; }
    public boolean isSpawnEnabled() { return spawnEnabled; }
    public Location getSpawnLocation() { return spawnLocation; }
    public boolean isPreventExplosionGriefing() { return preventExplosionGriefing; }
    public boolean isPreventFireSpread() { return preventFireSpread; }
    public boolean isPreventLeafDecay() { return preventLeafDecay; }
}