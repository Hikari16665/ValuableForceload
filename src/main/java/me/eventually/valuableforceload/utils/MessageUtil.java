package me.eventually.valuableforceload.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtil {

    public static void sendMessage(@NotNull Player p, @NotNull String message) {
        message = I18nUtil.get("prefix") + message;
        p.sendMessage(message.replace("&", "§"));
    }
    public static void sendActionBar(@NotNull Player p, @NotNull String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message.replace("&", "§")));
    }
    public static void sendTitle(@NotNull Player p, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        p.sendTitle(title.replace("&", "§"), subtitle.replace("&", "§"), fadeIn, stay, fadeOut);
    }
    public static void sendTitle(@NotNull Player p, @NotNull String title) {
        sendTitle(p, title.replace("&", "§"), "", 10, 70, 20);
    }
    public static void broadcastMessage(@NotNull String message) {
        message = I18nUtil.get("prefix") + message;
        @NotNull String finalMessage = message;
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(finalMessage.replace("&", "§")));
    }
    public static void broadcastMessage(@NotNull String message, @NotNull World world) {
        message = I18nUtil.get("prefix") + message;
        @NotNull String finalMessage = message;
        world.getPlayers().forEach(player -> player.sendMessage(finalMessage.replace("&", "§")));
    }
}
