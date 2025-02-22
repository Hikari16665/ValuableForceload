package me.eventually.valuableforceload.manager;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerChunkLimitManager {
    public static int DEFAULT_LIMIT;
    public static void setDefaultLimit(int defaultLimit) {
        DEFAULT_LIMIT = defaultLimit;
    }
    public static int getDefaultLimit() { return DEFAULT_LIMIT;}

    /**
     * <h1>A map of players and their chunk limit
     * Pair(L: current, R: max).</h1>
     */
    private static Map<UUID, Pair<Integer, Integer>> playerChunkLimit = new HashMap<>();

    public static void setPlayerChunksCurrent(OfflinePlayer player, int current) {
        Integer max = playerChunkLimit.get(player.getUniqueId()).getRight();
        playerChunkLimit.put(player.getUniqueId(), Pair.of(current, max));
    }
    public static int getPlayerChunksCurrent(OfflinePlayer player) {
        return playerChunkLimit.get(player.getUniqueId()).getLeft();
    }
    public static int getPlayerChunkLimit(OfflinePlayer player) {
        return playerChunkLimit.get(player.getUniqueId()).getRight();
    }
    public static void setPlayerChunkLimit(OfflinePlayer player, int limit) {
        Integer current = playerChunkLimit.get(player.getUniqueId()).getLeft();
        playerChunkLimit.put(player.getUniqueId(), Pair.of(current, limit));
    }
    public static void initPlayerChunkLimit(OfflinePlayer player) {
        if (!playerChunkLimit.containsKey(player.getUniqueId())) {
            playerChunkLimit.put(player.getUniqueId(), Pair.of(0, getDefaultLimit()));
        }
    }
    /**
     * Checks if the current number of chunks is less than or equal to the player's chunk limit.
     * @param player The player to check.
     *
     * @return True if the current number of chunks is less than or equal to the player's chunk limit, false otherwise.
     */
    public static Boolean checkPlayerChunkLimit(OfflinePlayer player) {
        return getPlayerChunksCurrent(player) < getPlayerChunkLimit(player);
    }

    public static void addPlayerChunk(OfflinePlayer player) {
        int current = getPlayerChunksCurrent(player);
        setPlayerChunksCurrent(player, current + 1);
    }
    public static void eraseAll() {
        playerChunkLimit.clear();
        Bukkit.getServer().getOnlinePlayers().forEach(PlayerChunkLimitManager::initPlayerChunkLimit);
    }
    public static void update(Player player){
        setPlayerChunksCurrent(player, ForceloadChunkManager.getForceloadChunkData(player).size());
    }
}
