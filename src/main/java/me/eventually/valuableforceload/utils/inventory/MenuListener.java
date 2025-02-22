package me.eventually.valuableforceload.utils.inventory;

import me.eventually.valuableforceload.utils.handlers.MenuCloseHandler;
import me.eventually.valuableforceload.utils.handlers.MenuOpenHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void
    onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Menu menu) {
            MenuItemStack item = menu.getItem(event.getSlot());
            if (item != null) {
                item.getClickHandler().onClick(event, event.getSlot(), menu);
            }

        }
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (event.getInventory().getHolder() instanceof Menu menu) {
            MenuOpenHandler openHandler = menu.getMenuOpenHandler();
            openHandler.onOpen(event, (Player) event.getPlayer(), menu);
            ((Player) event.getPlayer()).updateInventory();
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (event.getInventory().getHolder() instanceof Menu menu) {
            MenuCloseHandler closeHandler = menu.getMenuCloseHandler();
            closeHandler.onClose(event, (Player) event.getPlayer(), menu);
        }
    }
}
