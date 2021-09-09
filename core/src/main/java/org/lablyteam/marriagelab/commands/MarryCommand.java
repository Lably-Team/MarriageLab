package org.lablyteam.marriagelab.commands;

import lombok.RequiredArgsConstructor;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.gender.Gender;
import org.lablyteam.marriagelab.storage.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MarryCommand implements CommandClass {

    private final MarriageLab plugin;

    @Command(names = {"", "help"})
    public boolean onMainSubCommand(@Sender Player sender) {
        sender.sendMessage(plugin.getLanguage().getString("language.marry.main_subcommand"));
        return true;
    }

    @Command(names = "gender")
    public boolean onGenderSubCommand(@Sender Player sender, @OptArg("") String gender) {
        switch(gender.toLowerCase()) {
            case "male": {
                updateGender(Gender.MALE, sender);
                return true;
            }
            case "female": {
                updateGender(Gender.FEMALE, sender);
                return true;
            }
            case "other": {
                if(!plugin.getConfig().getBoolean("config.gender.enable_other")) {
                    sender.sendMessage(plugin.getLanguage().getString("language.gender.usage_other_disabled"));
                    return false;
                }
                updateGender(Gender.OTHER, sender);
                return true;
            }
            case "none":
            case "unspecified": {
                updateGender(Gender.UNSPECIFIED, sender);
                return true;
            }
            default: {
                sender.sendMessage(
                        plugin.getConfig().getBoolean("config.gender.enable_other")
                                ? plugin.getLanguage().getString("language.gender.usage_other_disabled")
                                : plugin.getLanguage().getString("language.gender.usage")
                );
                return false;
            }
        }
    }

    @Command(names = "block")
    public boolean onBlockSubCommand(@Sender Player sender, @OptArg("") String target) {
        User user = plugin.getDataManager().find(sender.getUniqueId());
        List<String> blockedPlayers = Arrays.asList(user.getBlockedPlayers());

        if(blockedPlayers.contains(target.toLowerCase())) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.block.already_blocked")
                    .replace("%player%", target)
            );
            return false;
        }

        blockedPlayers.add(target.toLowerCase());
        user.setBlockedPlayers(blockedPlayers.toArray(new String[0]));

        sender.sendMessage(plugin.getLanguage().getString("language.marry.block.player_blocked")
                .replace("%player%", target)
        );
        return true;
    }

    @Command(names = "unblock")
    public boolean onUnblockSubCommand(@Sender Player sender, @OptArg("") String target) {
        User user = plugin.getDataManager().find(sender.getUniqueId());
        List<String> blockedPlayers = Arrays.asList(user.getBlockedPlayers());

        if(!blockedPlayers.contains(target.toLowerCase())) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.unblock.not_blocked")
                    .replace("%player%", target)
            );
            return false;
        }

        blockedPlayers.remove(target.toLowerCase());
        user.setBlockedPlayers(blockedPlayers.toArray(new String[0]));

        sender.sendMessage(plugin.getLanguage().getString("language.marry.block.player_blocked")
                .replace("%player%", target)
        );
        return true;
    }

    @Command(names = "request")
    public boolean onRequestSubCommand(@Sender Player sender, @OptArg("") String receiver) {
        if(receiver.isEmpty()) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.empty_receiver"));
            return false;
        }

        Player target = Bukkit.getPlayer(receiver);
        if(target == null) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.receiver_offline"));
            return false;
        }

        plugin.getRequestManager().addRequest(sender, target);
        return true;
    }

    @Command(names = "accept")
    public boolean onAcceptSubCommand(@Sender Player sender, @OptArg("") String target) {
        if(target.isEmpty()) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.empty_requester"));
            return false;
        }

        OfflinePlayer requester = Bukkit.getPlayer(target);
        UUID from = requester.getUniqueId();

        if(!plugin.getRequestManager().hasPendingRequests(from)) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
            return false;
        }

        UUID to = plugin.getRequestManager().getRequest(from);
        if(!to.equals(sender.getUniqueId())) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
            return false;
        }

        plugin.getRequestManager().acceptRequest(from);
        return true;
    }

    @Command(names = "deny")
    public boolean onDenySubCommand(@Sender Player sender, @OptArg("") String target) {
        if(target.isEmpty()) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.empty_requester"));
            return false;
        }

        OfflinePlayer requester = Bukkit.getPlayer(target);
        UUID from = requester.getUniqueId();

        if(!plugin.getRequestManager().hasPendingRequests(from)) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
            return false;
        }

        UUID to = plugin.getRequestManager().getRequest(from);
        if(!to.equals(sender.getUniqueId())) {
            sender.sendMessage(plugin.getLanguage().getString("language.marry.request.invalid_request"));
            return false;
        }

        plugin.getRequestManager().denyRequest(from);
        return true;
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
}
