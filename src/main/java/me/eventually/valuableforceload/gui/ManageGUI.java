package me.eventually.valuableforceload.gui;

import me.eventually.valuableforceload.HotHandlers;
import me.eventually.valuableforceload.manager.ForceloadChunkManager;
import me.eventually.valuableforceload.manager.PlayerChunkLimitManager;
import me.eventually.valuableforceload.structure.ForceloadChunkData;
import me.eventually.valuableforceload.utils.I18nUtil;
import me.eventually.valuableforceload.utils.ItemStackUtil;
import me.eventually.valuableforceload.utils.LocationUtil;
import me.eventually.valuableforceload.utils.MessageUtil;
import me.eventually.valuableforceload.utils.inventory.MatrixDrawer;
import me.eventually.valuableforceload.utils.inventory.Menu;
import me.eventually.valuableforceload.utils.inventory.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageGUI extends AbstractGUI {

    public static Material BACKGROUND_MATERIAL = Material.BLACK_STAINED_GLASS_PANE;

    private final ItemStack BACKGROUND = ItemStackUtil.getItemStack(BACKGROUND_MATERIAL, 1, " ", " ");
    private ItemStack PLAYER_PROFILE = ItemStackUtil.getItemStack(Material.ACACIA_SIGN, 1, I18nUtil.get("player-profile", ""), " ");
    private final ItemStack BACK = ItemStackUtil.getItemStack(Material.WHITE_STAINED_GLASS_PANE, 1, I18nUtil.get("back"), "", I18nUtil.get("click-back"));
    private final ItemStack NEXT_PAGE = ItemStackUtil.getItemStack(Material.ARROW, 1, I18nUtil.get("next-page"), "");
    private final ItemStack LAST_PAGE = ItemStackUtil.getItemStack(Material.ARROW, 1, I18nUtil.get("last-page"), "");
    private static final Map<Integer, Integer> resultMap = new HashMap<>();

    static {
        resultMap.put(1, 10);
        resultMap.put(2, 11);
        resultMap.put(3, 12);
        resultMap.put(4, 13);
        resultMap.put(5, 14);
        resultMap.put(6, 15);
        resultMap.put(7, 16);
        resultMap.put(8, 19);
        resultMap.put(9, 20);
        resultMap.put(10, 21);
        resultMap.put(11, 22);
        resultMap.put(12, 23);
        resultMap.put(13, 24);
        resultMap.put(14, 25);
        resultMap.put(15, 28);
        resultMap.put(16, 29);
        resultMap.put(17, 30);
        resultMap.put(18, 31);
        resultMap.put(19, 32);
        resultMap.put(20, 33);
        resultMap.put(21, 34);
        resultMap.put(22, 37);
        resultMap.put(23, 38);
        resultMap.put(24, 39);
        resultMap.put(25, 40);
        resultMap.put(26, 41);
        resultMap.put(27, 42);
        resultMap.put(28, 43);
    }

    private MatrixDrawer drawer = new MatrixDrawer(54)
            .addLine("OOOOFOOOO")
            .addLine("         ")
            .addLine("         ")
            .addLine("         ")
            .addLine("         ")
            .addLine("OOOOBOOOO")
            .addExplain('O', BACKGROUND, HotHandlers.voidHandler)
            .addExplain('B', BACK, HotHandlers.mainMenuHandler);
    private static Menu menu;
    private int page = 1;
    @Override
    public void open(Player player){
        open(player, true);
    }
    public void open(Player player, boolean message) {
        //setup player profile display item
        ItemStackUtil.rename(PLAYER_PROFILE, I18nUtil.get("player-profile", player.getName()));
        ItemStackUtil.setLore(PLAYER_PROFILE,
                I18nUtil.get("forceload-chunks",
                        PlayerChunkLimitManager.getPlayerChunksCurrent(player),
                        PlayerChunkLimitManager.getPlayerChunkLimit(player)));
        //chunk data
        List<ForceloadChunkData> chunks = ForceloadChunkManager.getForceloadChunkData(player);
        if (chunks.isEmpty()){
            if (message) {
                MessageUtil.sendMessage(player, I18nUtil.get("no-forceload-chunks"));
            }
            return;
        }
        //build menu
        build();
        int chunksize = 28;
        List<List<ForceloadChunkData>> pages = new ArrayList<>();
        //create pages
        for (int i = 0; i< chunks.size(); i += chunksize){
            int end = Math.min(i + chunksize, chunks.size());
            pages.add(chunks.subList(i, end));
        }
        List<ForceloadChunkData> pageChunks = pages.get(page - 1);
        for (int i = 0; i < pageChunks.size(); i++) {
            //display every chunk
            ForceloadChunkData chunk = pageChunks.get(i);
            World world = Bukkit.getServer().getWorld(chunk.getWorld());
            
            //info show item
            ItemStack infoShowItem = ItemStackUtil.getItemStack(LocationUtil.getWorldDisplayMaterial(world), 1, I18nUtil.get(
                    "chunk",
                    chunk.getWorld(),
                    chunk.getX(),
                    chunk.getZ()
            ), chunk.getUniqueId());
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());
            Instant instant = Instant.ofEpochSecond(chunk.getExpireTimestamp());
            ItemStackUtil.addLoreLines(infoShowItem, I18nUtil.get("expire-date", formatter.format(instant)));
            ItemStackUtil.addLoreLines(infoShowItem, "", I18nUtil.get("click-delete"));
            ItemStackUtil.addLoreLines(infoShowItem, "", "&e点击删除!");

            //Menu item with delete handler
            MenuItem item = new MenuItem(infoShowItem, (event, slot, menu) ->{
                event.setCancelled(true);
                boolean removed = ForceloadChunkManager.removeForceloadChunk(chunk.getUniqueId());
                if (removed) {
                    PlayerChunkLimitManager.update(player);
                    refresh(player);
                } else {
                    MessageUtil.sendMessage(player, I18nUtil.get("remove-forceload-chunk-failed"));
                }
            });
            //add item to menu
            menu.setItem(resultMap.get(i + 1), item);

            //player profile
            menu.setItem(4, new MenuItem(PLAYER_PROFILE, HotHandlers.voidHandler));

            //page control buttons
            if (page > 1) {
                menu.setItem(47, new MenuItem(LAST_PAGE, (event, slot, menu) -> {
                    event.setCancelled(true);
                    page--;
                    refresh(player);
                }));
            }
            if (page < pages.size()) {
                menu.setItem(51, new MenuItem(NEXT_PAGE, (event, slot, menu) -> {
                    event.setCancelled(true);
                    page++;
                    refresh(player);
                }));
            }

            //open menu
            player.openInventory(menu.getInventory());
        }
    }
    public void refresh(Player player) {
        player.closeInventory();
        open(player, false);
    }

    @Override
    public void build() {
        menu = Menu.builder()
                .rows(6)
                .title(I18nUtil.getWithPrefix("manage-menu"))
                .drawer(drawer)
                .build();
    }
}
