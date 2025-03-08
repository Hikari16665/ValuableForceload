package me.eventually.valuableforceload.gui;

import me.eventually.valuableforceload.HotHandlers;
import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.exceptions.NotSetupException;
import me.eventually.valuableforceload.exceptions.NotSupportedCurrentException;
import me.eventually.valuableforceload.manager.EconomyManager;
import me.eventually.valuableforceload.manager.ForceloadChunkManager;
import me.eventually.valuableforceload.manager.PlayerChunkLimitManager;
import me.eventually.valuableforceload.structure.ForceloadChunk;
import me.eventually.valuableforceload.structure.Price;
import me.eventually.valuableforceload.utils.I18nUtil;
import me.eventually.valuableforceload.utils.ItemStackUtil;
import me.eventually.valuableforceload.utils.LocationUtil;
import me.eventually.valuableforceload.utils.MessageUtil;
import me.eventually.valuableforceload.utils.handlers.MenuClickHandler;
import me.eventually.valuableforceload.utils.inventory.MatrixDrawer;
import me.eventually.valuableforceload.utils.inventory.Menu;
import me.eventually.valuableforceload.utils.inventory.MenuItem;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainGUI extends AbstractGUI {

    public static Material BACKGROUND_MATERIAL = Material.BLACK_STAINED_GLASS_PANE;
    public static Material BUY_MATERIAL = Material.BEACON;
    public static Material LOOK_MATERIAL = Material.COMPASS;

    private final ItemStack BACKGROUND = ItemStackUtil.getItemStack(BACKGROUND_MATERIAL, 1, " ", " ");
    private ItemStack PLAYER_PROFILE = ItemStackUtil.getItemStack(Material.ACACIA_SIGN, 1, I18nUtil.get("player-profile", ""), " ");
    private final ItemStack QUIT = ItemStackUtil.getItemStack(Material.ARROW, 1, I18nUtil.get("quit"), I18nUtil.get("click-quit"));
    private final ItemStack BUY = ItemStackUtil.getItemStack(BUY_MATERIAL, 1, I18nUtil.get("buy"), I18nUtil.get("click-buy"));
    private final ItemStack LOOK = ItemStackUtil.getItemStack(LOOK_MATERIAL, 1, I18nUtil.get("look"), I18nUtil.get("click-look"));
    private final ItemStack AUTHOR = ItemStackUtil.getItemStack(Material.BOOK, 1, I18nUtil.get("author-list"), I18nUtil.get("author"));
    private final MatrixDrawer drawer = new MatrixDrawer(45)
            .addLine("AOOOFOOOO")
            .addLine("O.......O")
            .addLine("O.C...L.O")
            .addLine("O.......O")
            .addLine("OOOOQOOOO")
            .addExplain('O', BACKGROUND, HotHandlers.voidHandler)
            .addExplain('Q', QUIT, HotHandlers.closeHandler)
            .addExplain('F', PLAYER_PROFILE, HotHandlers.voidHandler)
            .addExplain('C', BUY, HotHandlers.voidHandler)
            .addExplain('A', AUTHOR, HotHandlers.voidHandler)
            .addExplain('L', LOOK, new MenuClickHandler() {
                @Override
                public void onClick(InventoryClickEvent event, int slot, Menu menu) {
                    event.setCancelled(true);
                    ManageGUI gui = new ManageGUI();
                    gui.open((Player) event.getWhoClicked());
                }
            });

    private static Menu menu;

    @Override
    public void open(Player player) {
        ItemStackUtil.rename(PLAYER_PROFILE, I18nUtil.get("player-profile", player.getName()));
        ItemStackUtil.setLore(PLAYER_PROFILE,
                I18nUtil.get("forceload-chunks",
                    PlayerChunkLimitManager.getPlayerChunksCurrent(player),
                    PlayerChunkLimitManager.getPlayerChunkLimit(player)));
        ItemStackUtil.setLore(BUY, I18nUtil.get("click-buy"));
        ItemStackUtil.addLoreLines(BUY, I18nUtil.get("price", EconomyManager.getPriceType().getDisplayName(), EconomyManager.getPrice(), EconomyManager.getBuyTimeOnce()));
        build();
        menu.setItem(4, new MenuItem(PLAYER_PROFILE, HotHandlers.voidHandler));
        menu.setItem(20, new MenuItem(BUY, buyHandler));

        player.openInventory(menu.getInventory());
    }
    public void refresh(Player player) {
        player.closeInventory();
        open(player);
    }
    @Override
    public void build() {
        menu = Menu.builder()
                .rows(5)
                .drawer(drawer)
                .title(I18nUtil.getWithPrefix("main-menu"))
                .build();
    }

    private final MenuClickHandler buyHandler = (event, slot, menu) -> {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        Price price = EconomyManager.createPrice();
        try {
            if (price.checkEnough(player)) {
                if (PlayerChunkLimitManager.checkPlayerChunkLimit(player)) {
                    if (!ForceloadChunkManager.isForceloaded(LocationUtil.getChunk(player.getLocation()))) {
                        price.pay(player);
                        Chunk chunk = LocationUtil.getChunk(player.getLocation());
                        ForceloadChunkManager.newForceloadChunk(new ForceloadChunk(
                                player,
                                price,
                                EconomyManager.getBuyTimeOnce() * 24 * 60 * 60,
                                chunk
                        ));
                        PlayerChunkLimitManager.addPlayerChunk(player);
                        MessageUtil.sendMessage(player, I18nUtil.get("buy-successful"));
                        ValuableForceload.getInstance().getLogger().info("Player %s created new forceload chunk %s".formatted(player.getName(), I18nUtil.get(
                                "chunk",
                                chunk.getWorld(),
                                chunk.getX(),
                                chunk.getZ()
                        )));
                        this.refresh(player);
                    }else{
                        MessageUtil.sendMessage(player, I18nUtil.get("cannot-buy-already-forceloaded"));
                    }
                }else{
                    MessageUtil.sendMessage(player, I18nUtil.get("cannot-buy-limit-reached"));
                }
            }else {
                MessageUtil.sendMessage(player, I18nUtil.get("cannot-buy-no-money"));
            }
        } catch (NotSetupException e) {
            MessageUtil.sendMessage(player, I18nUtil.get("economy-not-setup"));
            throw new RuntimeException(e);
        } catch (NotSupportedCurrentException wtf) {
            MessageUtil.sendMessage(player, I18nUtil.get("economy-not-supported"));
            throw new RuntimeException(wtf);
        }
    };
}
