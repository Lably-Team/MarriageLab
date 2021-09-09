package org.lablyteam.marriagelab.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.storage.model.User;
import org.lablyteam.marriagelab.utils.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestManagerImpl implements RequestManager {

    private final MarriageLab plugin;
    private final Map<UUID, UUID> requests;

    public RequestManagerImpl(MarriageLab plugin) {
        this.plugin = plugin;
        this.requests = new HashMap<>();
    }

    public boolean hasPendingRequests(UUID uuid) {
        return requests.containsKey(uuid);
    }

    public void addRequest(Player from, Player to) {
        requests.put(from.getUniqueId(), to.getUniqueId());

        from.sendMessage(plugin.getLanguage().getString("language.marry.request.request_sent")
                .replace("%player%", to.getName())
        );

        to.sendMessage(plugin.getLanguage().getString("language.marry.request.request_received")
                .replace("%player%", from.getName())
        );

        if(plugin.getConfig().getBoolean("config.requests.enable_expiration")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                    () -> requests.remove(from),
                    plugin.getConfig().getLong("config.requests.expire_time") * 20L
            );
        }
    }

    public void addRequest(UUID from, UUID to) {
        Player requester = Bukkit.getPlayer(from);
        Player receiver = Bukkit.getPlayer(to);

        if(requester == null || receiver == null) {
            throw new NullPointerException("Users must be online in order to send requests");
        }

        addRequest(requester, receiver);
    }

    public void cancelRequest(UUID from) {
        requests.remove(from);
    }

    @Override
    public UUID getRequest(UUID from) {
        return requests.get(from);
    }

    @Override
    public void acceptRequest(UUID from) {
        UUID to = requests.get(from);
        if(!hasPendingRequests(from)) {
            throw new IllegalArgumentException("Invalid request");
        }

        OfflinePlayer requester = Bukkit.getOfflinePlayer(from);
        OfflinePlayer receiver = Bukkit.getOfflinePlayer(to);

        cancelRequest(from);

        // Requester may have disconnected, and we would be sending a message to a non-existent player
        if(requester.isOnline()) {
            requester.getPlayer().sendMessage(
                    plugin.getLanguage().getString("language.requests.request_accepted")
                            .replace("%player%", receiver.getName())
            );
        }

        // This should always be true but just in case
        if(receiver.isOnline()) {
            receiver.getPlayer().sendMessage(
                    plugin.getLanguage().getString("language.requests.proposal_accepted")
                            .replace("%player%", requester.getName())
            );
        }

        User requesterUser = plugin.getDataManager().find(from);
        User receiverUser = plugin.getDataManager().find(to);

        requesterUser.setPartner(receiver.getName());
        receiverUser.setPartner(requester.getName());

        plugin.getDataManager().save(from, requesterUser);
        plugin.getDataManager().save(to, requesterUser);
    }

    @Override
    public void denyRequest(UUID from) {

    }
}
