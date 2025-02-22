package me.eventually.valuableforceload.utils.inventory;

public interface MenuDrawer {
    void draw(Menu menu);

    MenuDrawer EMPTY = menu -> {};
}
