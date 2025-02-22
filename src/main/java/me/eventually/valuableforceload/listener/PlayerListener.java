package me.eventually.valuableforceload.listener;

import me.eventually.valuableforceload.manager.PlayerChunkLimitManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerChunkLimitManager.initPlayerChunkLimit(event.getPlayer());
    }

}
