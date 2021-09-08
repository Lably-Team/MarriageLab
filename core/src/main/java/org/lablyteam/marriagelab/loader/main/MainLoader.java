package org.lablyteam.marriagelab.loader.main;

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

    private void loadLoaders(Loader... loaders) {
        for(Loader loader : loaders) {
            loader.load();
        }
    }
}
