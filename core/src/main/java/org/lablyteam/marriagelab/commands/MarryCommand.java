package org.lablyteam.marriagelab.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.gender.Gender;
import org.lablyteam.marriagelab.storage.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MarryCommand implements CommandExecutor {

    private final MarriageLab plugin;

    public MarryCommand(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player in order to use that command!");
            return false;
        }
        Player player = (Player) commandSender;
        if(args.length < 1) {
            player.sendMessage(plugin.getLanguage().getString("language.marry.main_subcommand"));
            return false;
        }

        switch(args[0].toLowerCase()) {
            case "gender": {
                if (args.length < 2) {
                    player.sendMessage(
                            plugin.getConfig().getBoolean("config.gender.enable_other")
                                    ? plugin.getLanguage().getString("language.marry.gender.usage")
                                    : plugin.getLanguage().getString("language.marry.gender.usage_other_disabled")
                    );
                    return false;
                }

                switch (args[1].toLowerCase()) {
                    case "male": {
                        updateGender(Gender.MALE, player);
                        return true;
                    }
                    case "female": {
                        updateGender(Gender.FEMALE, player);
                        return true;
                    }
                    case "other": {
                        if (!plugin.getConfig().getBoolean("config.gender.enable_other")) {
                            player.sendMessage(plugin.getLanguage().getString("language.marry.gender.usage_other_disabled"));
                            return false;
                        }
                        updateGender(Gender.OTHER, player);
                        return true;
                    }
                    case "none":
                    case "unspecified": {
                        updateGender(Gender.UNSPECIFIED, player);
                        return true;
                    }
                    default: {
                        player.sendMessage(
                                plugin.getConfig().getBoolean("config.gender.enable_other")
                                        ? plugin.getLanguage().getString("language.marry.gender.usage")
                                        : plugin.getLanguage().getString("language.marry.gender.usage_other_disabled")
                        );
                        return false;
                    }
                }
            }
            case "accept": {
                if (args.length < 2) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.empty_requester"));
                    return false;
                }

                String target = args[1];
                OfflinePlayer requester = Bukkit.getPlayer(target);
                UUID from = requester.getUniqueId();

                if (!plugin.getRequestManager().hasPendingRequests(from)) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
                    return false;
                }

                UUID to = plugin.getRequestManager().getRequest(from);
                if (!to.equals(player.getUniqueId())) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
                    return false;
                }

                plugin.getRequestManager().acceptRequest(from);
                return true;
            }
            case "deny": {
                if (args.length < 2) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.empty_requester"));
                    return false;
                }

                String target = args[1];
                OfflinePlayer requester = Bukkit.getPlayer(target);
                UUID from = requester.getUniqueId();

                if (!plugin.getRequestManager().hasPendingRequests(from)) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
                    return false;
                }

                UUID to = plugin.getRequestManager().getRequest(from);
                if (!to.equals(player.getUniqueId())) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
                    return false;
                }

                plugin.getRequestManager().denyRequest(from);
                return true;
            }
            case "request": {
                if(args.length < 2) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.empty_receiver"));
                    return false;
                }

                String receiver = args[1];
                Player target = Bukkit.getPlayer(receiver);
                if(target == null) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.receiver_offline"));
                    return false;
                }

                User user = plugin.getDataManager().find(player.getUniqueId());
                if(user.getPartner() != null && !user.getPartner().isEmpty()) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.already_married"));
                    return false;
                }

                User targetUser = plugin.getDataManager().find(target.getUniqueId());
                if(targetUser.getPartner() != null && !targetUser.getPartner().isEmpty()) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.request.target_already_married"));
                    return false;
                }

                plugin.getRequestManager().addRequest(player, target);
                return true;
            }
            case "divorce": {
                User user = plugin.getDataManager().find(player.getUniqueId());
                if(user.getPartner() == null || user.getPartner().isEmpty()) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.divorce.not_married"));
                    return false;
                }

                OfflinePlayer partner = Bukkit.getOfflinePlayer(user.getPartner());
                User partnerUser = plugin.getDataManager().find(partner.getUniqueId());

                partnerUser.setPartner("");
                user.setPartner("");

                plugin.getDataManager().save(partner.getUniqueId(), partnerUser);
                plugin.getDataManager().save(player.getUniqueId(), user);

                if(partner.isOnline()) {
                    player.sendMessage(plugin.getLanguage().getString("language.marry.divorce.partner_divorced"));
                    return false;
                }

                player.sendMessage(plugin.getLanguage().getString("language.marry.divorce.divorced"));
                broadcastDivorce(Bukkit.getOfflinePlayer(player.getUniqueId()), partner);
                return true;
            }
            case "help":
            default: {
                player.sendMessage(plugin.getLanguage().getString("language.marry.main_subcommand"));
                return false;
            }
        }
    }

    private void updateGender(Gender gender, Player player) {
        UUID uuid = player.getUniqueId();
        User user = plugin.getDataManager().find(uuid);

        user.setGender(gender);
        plugin.getDataManager().save(uuid, user);

        player.sendMessage(
                plugin.getLanguage().getString(
                        "language.marry.gender.updated_" + gender.name().toLowerCase()
                )
        );
    }

    private void broadcastDivorce(OfflinePlayer sender, OfflinePlayer partner) {
        Bukkit.getOnlinePlayers().forEach((Player player) -> {
            UUID uuid = player.getUniqueId();

            if(uuid.equals(sender.getUniqueId()) || uuid.equals(partner.getUniqueId())) {
                return;
            }

            player.sendMessage(plugin.getLanguage().getString("language.marry.divorced_broadcast")
                    .replace("%player1%", sender.getName())
                    .replace("%player2%", partner.getName())
            );
        });
    }
}
