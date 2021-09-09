package org.lablyteam.marriagelab.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.lablyteam.marriagelab.MarriageLab;

public class MarriageLabCommand implements CommandExecutor {

    private final MarriageLab plugin;

    public MarriageLabCommand(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1 || !sender.hasPermission("marriagelab.reload")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&b&lMARRIAGELAB &8» &rThis server is currently running MarriageLab version &a" +
                            plugin.getDescription().getVersion() + " &rdeveloped by &aPabszito&r."
            ));
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage(plugin.getLanguage().getString("language.plugin_reloaded"));
            return true;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&b&lMARRIAGELAB &8» &rThis server is currently running MarriageLab version &a" +
                            plugin.getDescription().getVersion() + " &rdeveloped by &aPabszito&r."
            ));
            return false;
        }
    }
}
