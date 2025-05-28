package me.eventually.valuableforceload.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtil {
    public static void runIfPlayer(CommandSender sender, Runnable runnable){
        if (sender instanceof Player) {
            runnable.run();
        }else{
            sender.sendMessage("You must be a player to use this command.");
        }
    }
}
