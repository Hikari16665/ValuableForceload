package me.eventually.valuableforceload.utils.inventory;

import me.eventually.valuableforceload.utils.handlers.MenuClickHandler;
import me.eventually.valuableforceload.utils.handlers.MenuCloseHandler;
import me.eventually.valuableforceload.utils.handlers.MenuOpenHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu extends MenuInventoryHolder {
    private Inventory inventory;
    private String title = "";
    private int rows = -1;
    private MenuDrawer menuDrawer;
    private MenuOpenHandler menuOpenHandler;
    private MenuCloseHandler menuCloseHandler;

    private Map<Integer, MenuItemStack> items = new HashMap<>();

    public static void setupListeners(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    private Menu() {
        items = new HashMap<>();
        menuDrawer.draw(this);
    }

    @ParametersAreNonnullByDefault
    private Menu(String title, int rows, MenuOpenHandler menuOpenHandler, MenuCloseHandler menuCloseHandler, MenuDrawer menuDrawer) {
        this.title = title;
        this.rows = rows;
        this.menuOpenHandler = menuOpenHandler;
        this.menuCloseHandler = menuCloseHandler;
        this.menuDrawer = menuDrawer;
        initInventory();
        menuDrawer.draw(this);
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    private void initInventory() {
        if (rows == -1) {
            rows = (int) Math.ceil(items.size() / 9.0d);
        }
        rows = Math.min(rows, 6);
        inventory = Bukkit.createInventory(this, rows * 9, title.replace("&", "§"));
    }

    @Override
    public void setItem(int slot, MenuItemStack item) {
        if (inventory == null) {
            initInventory();
        }
        items.put(slot, item);
        inventory.setItem(slot, item == null ? null : item.getItemStack());

        for (Player player : getViewers()) {
            player.updateInventory();
        }
    }

    public void setItemClickHandler(int slot, MenuClickHandler clickHandler){
        items.get(slot).setClickHandler(clickHandler);
    }

    @Override
    public MenuItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null) {
            initInventory();
        }
        return inventory;
    }
    public int getSize() {
        if (inventory == null) {
            initInventory();
        }
        return inventory.getSize();
    }

    public List<Player> getViewers() {
        if (inventory == null) {
            initInventory();
            return new ArrayList<>();
        }

        return inventory.getViewers()
                .stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .toList();
    }

    public MenuOpenHandler getMenuOpenHandler() {
        return menuOpenHandler;
    }

    public MenuCloseHandler getMenuCloseHandler() {
        return menuCloseHandler;
    }

    public static class MenuBuilder {
        private boolean title$set;
        private String title$value;
        private boolean openHandler$set;
        private MenuOpenHandler openHandler$value;
        private boolean closeHandler$set;
        private MenuCloseHandler closeHandler$value;
        private boolean rows$set;
        private int rows$value;
        private boolean drawer$set;
        private MenuDrawer drawer$value;

        MenuBuilder() {
        }


        public MenuBuilder title(String title) {
            this.title$value = title;
            this.title$set = true;
            return this;
        }

        public MenuBuilder openHandler(MenuOpenHandler openHandler) {
            this.openHandler$value = openHandler;
            this.openHandler$set = true;
            return this;
        }

        public MenuBuilder closeHandler(MenuCloseHandler closeHandler) {
            this.closeHandler$value = closeHandler;
            this.closeHandler$set = true;
            return this;
        }

        public MenuBuilder rows(int rows) {
            this.rows$value = rows;
            this.rows$set = true;
            return this;
        }

        public MenuBuilder drawer(MenuDrawer drawer) {
            this.drawer$value = drawer;
            this.drawer$set = true;
            return this;
        }

        public Menu build() {

            String title = this.title$set ? this.title$value : "";
            MenuOpenHandler openHandler = this.openHandler$set ? this.openHandler$value : MenuOpenHandler.DEFAULT;
            MenuCloseHandler closeHandler = this.closeHandler$set ? this.closeHandler$value : MenuCloseHandler.DEFAULT;
            int rows = this.rows$set ? this.rows$value : -1;
            MenuDrawer drawer = this.drawer$set ? this.drawer$value : MenuDrawer.EMPTY;
            return new Menu(title, rows, openHandler, closeHandler, drawer);
        }
    }
}
