package me.eventually.valuableforceload.manager;

import me.eventually.valuableforceload.ValuableForceload;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerChunkLimitManager {
    public static int DEFAULT_LIMIT = 3;
    public static void setDefaultLimit(int defaultLimit) {
        DEFAULT_LIMIT = defaultLimit;
    }
    public static int getDefaultLimit() { return DEFAULT_LIMIT;}

    /**
     * <h1>A map of players and their chunk limit
     * Pair(L: current, R: max).</h1>
     */
    private static Map<UUID, Pair<Integer, Integer>> playerChunkLimit = new HashMap<>();
    /**
     * <h1>A map of permission nodes and their override chunk limit.</h1>
     */
    private static Map<String, Integer> permissionOverrides = new HashMap<>();

    public static void setPlayerChunksCurrent(@NotNull OfflinePlayer player, int current) {
        Integer max = playerChunkLimit.get(player.getUniqueId()).getRight();
        playerChunkLimit.put(player.getUniqueId(), Pair.of(current, max));
    }
    public static int getPlayerChunksCurrent(@NotNull OfflinePlayer player) {
        return playerChunkLimit.get(player.getUniqueId()).getLeft();
    }
    public static int getPlayerChunkLimit(@NotNull OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        Player p = ValuableForceload.getInstance().getServer().getPlayer(UUID.fromString(uuid));
        int offline_limit = playerChunkLimit.get(player.getUniqueId()).getRight();
        List<Integer> permission_override_limit = new ArrayList<>();
        if (p == null) {
            return offline_limit;
        }else{
            for (Map.Entry<String, Integer> entry : permissionOverrides.entrySet()) {
                if (p.hasPermission(entry.getKey())) {
                    permission_override_limit.add(entry.getValue());
                }
            }
        }
        return permission_override_limit.isEmpty() ? offline_limit : Collections.max(permission_override_limit);
    }
    public static void setPlayerChunkLimit(@NotNull OfflinePlayer player, int limit) {
        Integer current = playerChunkLimit.get(player.getUniqueId()).getLeft();
        playerChunkLimit.put(player.getUniqueId(), Pair.of(current, limit));
    }
    public static void initPlayerChunkLimit(@NotNull OfflinePlayer player) {
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
    public static @NotNull Boolean checkPlayerChunkLimit(OfflinePlayer player) {
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

    public static void parsePermissionOverrides(@NotNull List<Map<String, Object>> map) {
        permissionOverrides.clear();
        for (Map<String, Object> m : map){
            permissionOverrides.put((String) m.get("permission"), (Integer) m.get("limit"));
        }
    }
    public static @NotNull List<Map<String, Object>> mapPermissionOverrides() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : permissionOverrides.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("permission", entry.getKey());
            map.put("limit", entry.getValue());
            list.add(map);
        }
        return list;
    }
}
