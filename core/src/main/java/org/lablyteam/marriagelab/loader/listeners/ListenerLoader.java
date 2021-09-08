package org.lablyteam.marriagelab.loader.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.listeners.AsyncPlayerPreLoginListener;
import org.lablyteam.marriagelab.loader.Loader;

@RequiredArgsConstructor
public class ListenerLoader implements Loader {

    private final MarriageLab plugin;

    @Override
    public void load() {
        registerListener(
                // Built-in listeners
                new AsyncPlayerPreLoginListener(plugin)
        );
    }

    private void registerListener(Listener... listeners) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for(Listener listener : listeners) {
            pluginManager.registerEvents(listener, plugin);
        }
    }
}