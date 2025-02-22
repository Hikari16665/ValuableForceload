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
import java.util.List;
import java.util.Map;

public class ManageGUI extends AbstractGUI {
    private final ItemStack BACKGROUND = ItemStackUtil.getItemStack(Material.PINK_STAINED_GLASS_PANE, 1, " ", " ");
    private ItemStack PLAYER_PROFILE = ItemStackUtil.getItemStack(Material.ACACIA_SIGN, 1, I18nUtil.get("player-profile", ""), " ");
    private final ItemStack BACK = ItemStackUtil.getItemStack(Material.OAK_DOOR, 1, I18nUtil.get("back"), I18nUtil.get("click-quit"));
    private final ItemStack NEXT_PAGE = ItemStackUtil.getItemStack(Material.ARROW, 1, I18nUtil.get("next-page"), "");
    private final ItemStack LAST_PAGE = ItemStackUtil.getItemStack(Material.ARROW, 1, I18nUtil.get("last-page"), "");
    private static final Map<Integer, Integer> resultMap = Map.of(
            1, 20,
            2, 21,
            3, 22,
            4, 23,
            5, 24,
            6, 29,
            7, 30,
            8, 31,
            9, 32,
            10, 33
    );
    private MatrixDrawer drawer = new MatrixDrawer(54)
            .addLine("OOOOFOOOO")
            .addLine("O       O")
            .addLine("O       O")
            .addLine("O       O")
            .addLine("O       O")
            .addLine("OOOOBOOOO")
            .addExplain('O', BACKGROUND, HotHandlers.voidHandler)
            .addExplain('B', BACK, HotHandlers.mainMenuHandler);
    private static Menu menu;
    private int page = 1;
    @Override
    public void open(Player player) {
        ItemStackUtil.rename(PLAYER_PROFILE, I18nUtil.get("player-profile", player.getName()));
        ItemStackUtil.setLore(PLAYER_PROFILE,
                I18nUtil.get("forceload-chunks",
                        PlayerChunkLimitManager.getPlayerChunksCurrent(player),
                        PlayerChunkLimitManager.getPlayerChunkLimit(player)));
        List<ForceloadChunkData> chunks = ForceloadChunkManager.getForceloadChunkData(player);
        if (chunks.isEmpty()){
            MessageUtil.sendMessage(player, I18nUtil.get("no-forceload-chunks"));
            return;
        }
        build();
        int chunksize = 10;
        List<List<ForceloadChunkData>> pages = new ArrayList<>();
        for (int i = 0; i< chunks.size(); i += chunksize){
            int end = Math.min(i + chunksize, chunks.size());
            pages.add(chunks.subList(i, end));
        }
        List<ForceloadChunkData> pageChunks = pages.get(page - 1);
        for (int i = 0; i < pageChunks.size(); i++) {
            ForceloadChunkData chunk = pageChunks.get(i);
            World world = Bukkit.getServer().getWorld(chunk.getWorld());

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
            ItemStackUtil.addLoreLines(infoShowItem, I18nUtil.get("click-delete"));
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
            menu.setItem(resultMap.get(i + 1), item);
            menu.setItem(4, new MenuItem(PLAYER_PROFILE, HotHandlers.voidHandler));
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
            player.openInventory(menu.getInventory());
        }
    }
    public void refresh(Player player) {
        player.closeInventory();
        open(player);
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
