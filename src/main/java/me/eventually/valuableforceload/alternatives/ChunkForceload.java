package me.eventually.valuableforceload.alternatives;

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import me.eventually.valuableforceload.ValuableForceload;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class ChunkForceload {
    public static void setChunkForceload(Chunk chunk, boolean forceLoaded) {
        if (ValuableForceload.getInstance().isFolia) {
            GlobalRegionScheduler scheduler = Bukkit.getGlobalRegionScheduler();
            scheduler.execute(ValuableForceload.getInstance(), () -> {
                chunk.setForceLoaded(forceLoaded);
            });
        } else {
            chunk.setForceLoaded(forceLoaded);
        }
    }
}
