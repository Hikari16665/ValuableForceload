package me.eventually.valuableforceload.commands;

import me.eventually.valuableforceload.ValuableForceload;
import me.eventually.valuableforceload.commands.sub.AdminSubCommand;
import me.eventually.valuableforceload.gui.MainGUI;
import me.eventually.valuableforceload.utils.PlayerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MainCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        switch (args[0]) {
            case "open":
                if (sender.hasPermission("valuableforceload.command.gui")) {
                    MainGUI gui = new MainGUI();
                    PlayerUtil.runIfPlayer(sender, () -> gui.open((Player) sender));
                    return true;
                } else {
                    sender.sendMessage("You do not have permission to use this command.");
                }
                break;
            case "version":
                sender.sendMessage("ValuableForceload version " + ValuableForceload.getInstance().getDescription().getVersion());
                return true;
            case "admin":
                if (args.length < 2) return false;
                return AdminSubCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length){
            case 0, 1 -> {
                return List.of("open", "version", "admin");
            }
            case 2 -> {
                if (args[0].equals("admin")) {
                    return List.of("erase_all");
                }
            }
        }

        return List.of();
}

}
