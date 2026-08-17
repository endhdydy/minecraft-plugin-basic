package com.example.advanced;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpManager {

    private final Main plugin;
    private final Map<String, WarpLocation> warps = new HashMap<>();

    public record WarpLocation(String world, double x, double y, double z, float yaw, float pitch) {}

    public WarpManager(Main plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        warps.clear();
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("warps")) return;
        for (String key : config.getConfigurationSection("warps").getKeys(false)) {
            String path = "warps." + key;
            warps.put(key.toLowerCase(), new WarpLocation(
                config.getString(path + ".world"),
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"),
                (float) config.getDouble(path + ".yaw"),
                (float) config.getDouble(path + ".pitch")
            ));
        }
    }

    public void save() {
        for (Map.Entry<String, WarpLocation> e : warps.entrySet()) {
            String path = "warps." + e.getKey();
            WarpLocation w = e.getValue();
            plugin.getConfig().set(path + ".world", w.world());
            plugin.getConfig().set(path + ".x", w.x());
            plugin.getConfig().set(path + ".y", w.y());
            plugin.getConfig().set(path + ".z", w.z());
            plugin.getConfig().set(path + ".yaw", (double) w.yaw());
            plugin.getConfig().set(path + ".pitch", (double) w.pitch());
        }
        plugin.saveConfig();
    }

    public boolean create(String name, Player player) {
        String key = name.toLowerCase();
        if (warps.containsKey(key)) return false;
        Location loc = player.getLocation();
        warps.put(key, new WarpLocation(
            loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()
        ));
        save();
        return true;
    }

    public boolean remove(String name) {
        return warps.remove(name.toLowerCase()) != null;
    }

    public WarpLocation get(String name) {
        return warps.get(name.toLowerCase());
    }

    public Set<String> list() {
        return warps.keySet();
    }

    public boolean teleport(String name, Player player) {
        WarpLocation w = warps.get(name.toLowerCase());
        if (w == null) return false;
        org.bukkit.World world = plugin.getServer().getWorld(w.world());
        if (world == null) return false;
        player.teleport(new Location(world, w.x(), w.y(), w.z(), w.yaw(), w.pitch()));
        return true;
    }
}