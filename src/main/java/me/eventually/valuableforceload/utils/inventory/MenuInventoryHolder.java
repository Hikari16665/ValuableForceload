package me.eventually.valuableforceload.utils.inventory;

import org.bukkit.inventory.InventoryHolder;

public abstract class MenuInventoryHolder implements InventoryHolder {
    public abstract void setItem(int slot, MenuItemStack item);
    public abstract MenuItemStack getItem(int slot);
}
