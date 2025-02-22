package me.eventually.valuableforceload.utils;

import me.eventually.valuableforceload.ValuableForceload;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Map;

public class LocationUtil {
    public static Chunk getChunk(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        return ValuableForceload.getInstance().getServer().getWorld(world.getUID()).getChunkAt(x >> 4, y >> 4);
    }
    public static Map<String, Object> mapChunk(Chunk chunk) {
        return Map.of(
                "x", chunk.getX(),
                "z", chunk.getZ(),
                "world", chunk.getWorld().getName()
        );
    }
    public static Chunk parseMapChunk(Map<String, Object> map) {
        return ValuableForceload.getInstance().getServer().getWorld((String) map.get("world")).getChunkAt((int) map.get("x"), (int) map.get("z"));
    }
    public static Material getWorldDisplayMaterial(World world){
        if (world == null) return Material.BARRIER;
        switch (world.getEnvironment()){
            case NORMAL:
                return Material.GRASS_BLOCK;
            case NETHER:
                return Material.NETHERRACK;
            case THE_END:
                return Material.END_STONE;
            default:
                return Material.COMMAND_BLOCK;
        }
    }
}
