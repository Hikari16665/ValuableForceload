package me.eventually.valuableforceload.utils.handlers;

import me.eventually.valuableforceload.utils.inventory.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface MenuClickHandler {
    void onClick(InventoryClickEvent event, int slot, Menu menu);

    MenuClickHandler DEFAULT = (event, slot, menu) -> {};
}
