package me.eventually.valuableforceload.utils.inventory;

import me.eventually.valuableforceload.utils.handlers.MenuClickHandler;
import org.bukkit.inventory.ItemStack;

public class MenuItem implements MenuItemStack {
    private ItemStack item;
    private MenuClickHandler clickHandler;
    @Override
    public void setItemStack(ItemStack item) {
        this.item = item;
    }

    @Override
    public ItemStack getItemStack() {
        return item;
    }

    @Override
    public MenuClickHandler getClickHandler() {
        return clickHandler;
    }

    @Override
    public void setClickHandler(MenuClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }
    public MenuItem(ItemStack item, MenuClickHandler clickHandler) {
        this.item = item;
        this.clickHandler = clickHandler;
    }
}
