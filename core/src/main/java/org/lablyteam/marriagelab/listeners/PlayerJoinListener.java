package org.lablyteam.marriagelab.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.storage.model.User;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final MarriageLab plugin;

    public PlayerJoinListener(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if(plugin.getDataManager().contains(uuid)) {
            return;
        }

        plugin.getDataManager().save(uuid, new User(uuid));
    }
}
