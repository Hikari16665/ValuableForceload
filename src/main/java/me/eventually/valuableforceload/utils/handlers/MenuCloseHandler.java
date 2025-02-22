package me.eventually.valuableforceload.utils.handlers;

import me.eventually.valuableforceload.utils.inventory.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface MenuCloseHandler {
    void onClose(InventoryCloseEvent event, Player player, Menu menu);

    MenuCloseHandler DEFAULT = (event, player, menu) -> {};
}
