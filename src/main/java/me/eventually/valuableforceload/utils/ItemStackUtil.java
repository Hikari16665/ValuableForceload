package me.eventually.valuableforceload.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemStackUtil {
    public static ItemStack getItemStack(Material material, int amount, String name, String... lore) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name.replace('&', '§'));
        for (int i = 0; i < lore.length; i++) {
            lore[i] = "§7" + lore[i].replace('&', '§');
        }
        meta.setLore(List.of(lore));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static void rename(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name.replace('&', '§'));
        itemStack.setItemMeta(meta);
    }
    public static void setLore(ItemStack itemStack, String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        for (int i = 0; i < lore.length; i++) {
            lore[i] = "§7" + lore[i].replace('&', '§');
        }
        meta.setLore(List.of(lore));
        itemStack.setItemMeta(meta);
    }
    public static void addLoreLines(ItemStack itemStack, String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> loreList = meta.getLore();
        for (int i = 0; i < lore.length; i++) {
            lore[i] = "§7" + lore[i].replace('&', '§');
        }
        loreList.addAll(List.of(lore));
        meta.setLore(loreList);
        itemStack.setItemMeta(meta);
    }

}
