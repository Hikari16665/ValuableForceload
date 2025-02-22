package me.eventually.valuableforceload.commands.sub;

import me.eventually.valuableforceload.manager.ForceloadChunkManager;
import me.eventually.valuableforceload.manager.PlayerChunkLimitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AdminSubCommand {
    public static boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (!sender.hasPermission("valuableforceload.command.admin")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        switch (args[0]) {
            case "erase_all":
                ForceloadChunkManager.eraseAll();
                PlayerChunkLimitManager.eraseAll();
                sender.sendMessage("Erased all data in VFL.");
                return true;
        }
        return false;
    }
}
