package me.eventually.valuableforceload.utils.handlers;

import me.eventually.valuableforceload.utils.inventory.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface MenuOpenHandler {
    void onOpen(InventoryOpenEvent event, Player player, Menu menu);

    MenuOpenHandler DEFAULT = (event, player, menuDrawer) -> {};
}
