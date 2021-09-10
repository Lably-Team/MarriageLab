package org.lablyteam.marriagelab.loader.manager;

import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.manager.RequestManagerImpl;

public class ManagerLoader implements Loader {

    private final MarriageLab plugin;

    public ManagerLoader(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        plugin.setRequestManager(new RequestManagerImpl(plugin));
    }
}
