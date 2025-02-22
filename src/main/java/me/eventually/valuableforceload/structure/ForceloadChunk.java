package me.eventually.valuableforceload.structure;

import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.manager.PlayerChunkLimitManager;
import me.eventually.valuableforceload.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ForceloadChunk {

    private final String ownerUuid;
    private final Price price;

    private long expireTimestamp;
    private final Chunk chunk;



    private final String uniqueId;

    public ForceloadChunk(Player owner, Price price, int expireTimeSeconds, Chunk chunk) {
        this(owner.getUniqueId().toString(), price, expireTimeSeconds, chunk);
    }
    public ForceloadChunk(OfflinePlayer owner, Price price, int expireTimeSeconds, Chunk chunk) {
        this(owner.getUniqueId().toString(), price, expireTimeSeconds, chunk);
    }
    public ForceloadChunk(String ownerUuid, Price price, int expireTimeSeconds, Chunk chunk) {
        this.ownerUuid = ownerUuid;
        this.price = price;
        this.chunk = chunk;
        this.expireTimestamp = System.currentTimeMillis() / 1000L + expireTimeSeconds;
        this.uniqueId = UUID.randomUUID().toString();
        ValuableForceload.getInstance().getLogger().info("Created new forceload chunk: " + this);
    }
    public ForceloadChunk(String ownerUuid, Price price, long expireTimestamp, Chunk chunk, String uniqueId){
        this.ownerUuid = ownerUuid;
        this.price = price;
        this.expireTimestamp = expireTimestamp;
        this.chunk = chunk;
        this.uniqueId = uniqueId;
    }

    /**
     * Checks if the forceload chunk is expired.
     * @return true if the forceload chunk is expired, false otherwise.
     */
    public Boolean isExpired() {
        return this.expireTimestamp < System.currentTimeMillis() / 1000L;
    }
    public void renewSeconds(int seconds) {
        this.expireTimestamp += seconds;
    }
    public void renewDays(int days) {
        this.expireTimestamp += (long) days * 24 * 60 * 60;
    }

    public void setExpireTimestamp(int expireTimestamp) {
        this.expireTimestamp = expireTimestamp;
    }

    public long getExpireTimestamp() {
        return expireTimestamp;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public Price getPrice() {
        return price;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public Player getOwnerPlayer() {
        return ValuableForceload.getInstance().getServer().getPlayer(UUID.fromString(this.ownerUuid));
    }

    public void updateForceLoadStatus() {
        this.chunk.setForceLoaded(!this.isExpired());
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "ownerUuid", this.ownerUuid,
                "expireTimestamp", this.expireTimestamp,
                "chunk", LocationUtil.mapChunk(this.chunk),
                "uniqueId", this.uniqueId
        );
    }

    public static ForceloadChunk parseMap(Map<String, Object> map){
        String ownerUuid = (String) map.get("ownerUuid");
        UUID parsedOwnerUuid = java.util.UUID.fromString(ownerUuid);
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(parsedOwnerUuid);
        Bukkit.getLogger().info("Parsed ownerUuid: " + ownerUuid + "To Player " + player);
        if (player != null) {
            PlayerChunkLimitManager.initPlayerChunkLimit(player);
            PlayerChunkLimitManager.addPlayerChunk(player);
        }
        Price price = new Price();
        long expireTimestamp = ((Number) map.get("expireTimestamp")).longValue();
        Chunk chunk = LocationUtil.parseMapChunk((Map<String, Object>) map.get("chunk"));
        String uniqueId = map.containsKey("uniqueId") ? (String) map.get("uniqueId") : UUID.randomUUID().toString();
        return new ForceloadChunk(ownerUuid, price, expireTimestamp, chunk, uniqueId);
    }
}
