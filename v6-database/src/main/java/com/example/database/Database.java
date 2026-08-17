package com.example.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class Database {

    private final JavaPlugin plugin;
    private Connection connection;

    public Database(JavaPlugin plugin) throws SQLException {
        this.plugin = plugin;
        connect();
        createTables();
    }

    private void connect() throws SQLException {
        String path = plugin.getDataFolder().getAbsolutePath() + "/data.db";
        plugin.getDataFolder().mkdirs();
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        // WAL 모드로 성능 향상
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.execute("PRAGMA synchronous=NORMAL");
        }
    }

    private void createTables() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS players (
                uuid VARCHAR(36) PRIMARY KEY,
                name VARCHAR(16) NOT NULL,
                first_join BIGINT NOT NULL,
                last_join BIGINT NOT NULL,
                playtime_seconds BIGINT DEFAULT 0,
                kills INT DEFAULT 0,
                deaths INT DEFAULT 0,
                blocks_broken INT DEFAULT 0,
                blocks_placed INT DEFAULT 0
            )
            """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void upsertPlayer(String uuid, String name) throws SQLException {
        String sql = """
            INSERT INTO players (uuid, name, first_join, last_join)
            VALUES (?, ?, ?, ?)
            ON CONFLICT(uuid) DO UPDATE SET
                name = excluded.name,
                last_join = excluded.last_join
            """;
        long now = System.currentTimeMillis();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setLong(3, now);
            ps.setLong(4, now);
            ps.executeUpdate();
        }
    }

    public void addPlaytime(String uuid, long seconds) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE players SET playtime_seconds = playtime_seconds + ? WHERE uuid = ?")) {
            ps.setLong(1, seconds);
            ps.setString(2, uuid);
            ps.executeUpdate();
        }
    }

    public void addStat(String uuid, String stat, int amount) throws SQLException {
        String column = switch (stat) {
            case "kill" -> "kills";
            case "death" -> "deaths";
            case "break" -> "blocks_broken";
            case "place" -> "blocks_placed";
            default -> throw new IllegalArgumentException("Unknown stat: " + stat);
        };
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE players SET " + column + " = " + column + " + ? WHERE uuid = ?")) {
            ps.setInt(1, amount);
            ps.setString(2, uuid);
            ps.executeUpdate();
        }
    }

    public PlayerStats getStats(String uuid) throws SQLException {
        String sql = "SELECT * FROM players WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PlayerStats(
                        rs.getString("uuid"),
                        rs.getString("name"),
                        rs.getLong("playtime_seconds"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getInt("blocks_broken"),
                        rs.getInt("blocks_placed")
                    );
                }
            }
        }
        return null;
    }

    public record PlayerStats(String uuid, String name, long playtimeSeconds,
                              int kills, int deaths, int blocksBroken, int blocksPlaced) {
        public double getKdRatio() {
            return deaths == 0 ? kills : Math.round((double) kills / deaths * 100.0) / 100.0;
        }
        public String getPlaytimeFormatted() {
            long hours = playtimeSeconds / 3600;
            long minutes = (playtimeSeconds % 3600) / 60;
            return hours + "시간 " + minutes + "분";
        }
    }

    public void close() {
        try { if (connection != null && !connection.isClosed()) connection.close(); }
        catch (SQLException ignored) {}
    }
}