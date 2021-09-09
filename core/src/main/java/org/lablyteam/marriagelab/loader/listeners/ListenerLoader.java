package org.lablyteam.marriagelab.loader.listeners;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.listeners.PlayerJoinListener;
import org.lablyteam.marriagelab.loader.Loader;

public class ListenerLoader implements Loader {

    private final MarriageLab plugin;

    public ListenerLoader(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        registerListener(
                // Built-in listeners
                new PlayerJoinListener(plugin)
        );
    }

    private void registerListener(Listener... listeners) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for(Listener listener : listeners) {
            pluginManager.registerEvents(listener, plugin);
        }
    }
}
