package me.eventually.valuableforceload.gui;

import me.eventually.valuableforceload.utils.inventory.Menu;
import org.bukkit.entity.Player;

public abstract class AbstractGUI {
    private Menu menu;

    public abstract void open(Player player);
    

    public abstract void build();

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
