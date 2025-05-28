package me.eventually.valuableforceload.structure;

public class ForceloadChunkData{
    private final String uniqueId;
    private final String x;
    private final String z;
    private final String world;
    private final long expireTimestamp;

    public ForceloadChunkData(ForceloadChunk forceloadChunk){
        this.uniqueId = forceloadChunk.getUniqueId();
        this.x = String.valueOf(forceloadChunk.getChunk().getX());
        this.z = String.valueOf(forceloadChunk.getChunk().getZ());
        this.world = forceloadChunk.getChunk().getWorld().getName();
        this.expireTimestamp = forceloadChunk.getExpireTimestamp();
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getX() {
        return x;
    }

    public String getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public long getExpireTimestamp() {
        return expireTimestamp;
    }
}
