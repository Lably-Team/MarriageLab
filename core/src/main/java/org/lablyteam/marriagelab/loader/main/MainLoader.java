package org.lablyteam.marriagelab.loader.main;

import org.bukkit.Bukkit;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.loader.commands.CommandLoader;
import org.lablyteam.marriagelab.loader.listeners.ListenerLoader;

public class MainLoader implements Loader {

    private final MarriageLab plugin;

    public MainLoader(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        loadLoaders(
                new ListenerLoader(plugin),
                new CommandLoader(plugin)
        );
    }

    @Override
    public void unload() {
        if(Bukkit.getOnlinePlayers().size() > 0) {
            plugin.getLogger().warning("Reloading while players are connected is unsupported and will cause the plugin to not function properly. Beware.");
        }
    }

    private void loadLoaders(Loader... loaders) {
        for(Loader loader : loaders) {
            loader.load();
        }
    }
}
