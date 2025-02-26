package me.eventually.valuableforceload.manager;

import me.eventually.valuableforceload.structure.ForceloadChunk;
import me.eventually.valuableforceload.structure.ForceloadChunkData;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForceloadChunkManager {
    private static List<ForceloadChunk> forceloadChunks = new ArrayList<>();

    public static List<ForceloadChunk> getForceloadChunks() {
        if (forceloadChunks == null) {
            return List.of();
        }
        return forceloadChunks;
    }

    public static void newForceloadChunk(ForceloadChunk forceloadChunk) {
        forceloadChunks.add(forceloadChunk);
        updateChunkForceloadStatus();
    }

    public static void updateChunkForceloadStatus(){
        if (forceloadChunks == null || forceloadChunks.isEmpty()) {
            return;
        }
        for (ForceloadChunk forceloadChunk : forceloadChunks) {
            forceloadChunk.updateForceLoadStatus();
        }
    }

    public static void cleanUpExpiredForceloads(){
        if (forceloadChunks == null || forceloadChunks.isEmpty()) {
            return;
        }
        List<ForceloadChunk> toRemove = new ArrayList<>();
        for (ForceloadChunk forceloadChunk : forceloadChunks) {
            if (forceloadChunk.isExpired()) {
                toRemove.add(forceloadChunk);

            }
        }
        for (ForceloadChunk forceloadChunk : toRemove) {
            forceloadChunk.getChunk().setForceLoaded(false);
            forceloadChunks.remove(forceloadChunk);
        }
    }

    public static void unloadAll() {
        if (forceloadChunks == null || forceloadChunks.isEmpty()) {
            return;
        }
        for (ForceloadChunk forceloadChunk : forceloadChunks) {
            forceloadChunk.getChunk().setForceLoaded(false);
        }
    }

    public static boolean isForceloaded(Chunk chunk) {
        if (forceloadChunks == null || forceloadChunks.isEmpty()) {
            return false;
        }
        for (ForceloadChunk forceloadChunk : forceloadChunks) {
            if (forceloadChunk.getChunk().getX() == chunk.getX() && forceloadChunk.getChunk().getZ() == chunk.getZ()) {
                return true;
            }
        }
        return false;
    }

    public static List<Map<String, Object>> mapAllChunks(){
        List<Map<String, Object>> list = new ArrayList<>();
        for (ForceloadChunk chunk : forceloadChunks) {
            list.add(chunk.toMap());
        }
        return list;
    }
    public static void parseAllChunks(List<Map<String, Object>> list){
        if (list == null || list.isEmpty()) return;

        for (Map<String, Object> map : list) {
            ForceloadChunk chunk = ForceloadChunk.parseMap(map);
            forceloadChunks.add(chunk);
        }
    }

    public static void eraseAll() {
        if(forceloadChunks == null || forceloadChunks.isEmpty()) return;
        unloadAll();
        forceloadChunks.clear();
    }

    public static Boolean removeForceloadChunk(String uniqueId) {
        boolean removed = false;
        for (ForceloadChunk forceloadChunk : forceloadChunks) {
            if (forceloadChunk.getUniqueId().equals(uniqueId)) {
                forceloadChunk.getChunk().setForceLoaded(false);
                forceloadChunks.remove(forceloadChunk);
                updateChunkForceloadStatus();
                removed = true;
                break;
            }
        }
        return removed;
    }
    public static List<ForceloadChunkData> getForceloadChunkData(Player player) {
        if (forceloadChunks == null || forceloadChunks.isEmpty()) {
            return List.of();
        }
        List<ForceloadChunkData> list = new ArrayList<>();
        for (ForceloadChunk chunk : forceloadChunks) {
            Player owner = chunk.getOwnerPlayer();
            if (owner == null) continue;
            if (!(owner.getUniqueId() == player.getUniqueId())) continue;
            list.add(new ForceloadChunkData(chunk));
        }
        return list;
    }
}
