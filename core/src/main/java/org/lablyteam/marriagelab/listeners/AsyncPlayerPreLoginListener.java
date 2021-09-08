package org.lablyteam.marriagelab.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.storage.model.User;

import java.util.UUID;

@RequiredArgsConstructor
public class AsyncPlayerPreLoginListener implements Listener {

    private final MarriageLab plugin;

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        if(plugin.getDataManager().contains(uuid)) {
            return;
        }

        plugin.getDataManager().save(uuid, new User(uuid));
    }
}
