package me.eventually.valuableforceload.utils.inventory;

import me.eventually.valuableforceload.utils.handlers.MenuClickHandler;
import org.bukkit.inventory.ItemStack;

public interface MenuItemStack {
    void setItemStack(ItemStack item);
    ItemStack getItemStack();

    MenuClickHandler getClickHandler();
    void setClickHandler(MenuClickHandler clickHandler);
}
