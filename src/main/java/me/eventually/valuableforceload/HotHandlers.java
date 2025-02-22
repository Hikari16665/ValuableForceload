package me.eventually.valuableforceload;

import me.eventually.valuableforceload.gui.MainGUI;
import me.eventually.valuableforceload.utils.handlers.MenuClickHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.concurrent.CopyOnWriteArrayList;

public class HotHandlers {
    public static MenuClickHandler voidHandler = (event, slot, menu) -> event.setCancelled(true);
    public static MenuClickHandler closeHandler = (event, slot, menu) -> {
        event.setCancelled(true);
        CopyOnWriteArrayList<HumanEntity> entities = new CopyOnWriteArrayList<>(event.getViewers());
        entities.forEach((player) -> {
            Player _player = (Player) player;
            _player.closeInventory();
        });
    };
    public static MenuClickHandler mainMenuHandler = (event, slot, menu) -> {
        event.setCancelled(true);
        MainGUI mainGUI = new MainGUI();
        mainGUI.open((Player) event.getWhoClicked());
    };

}
