package org.lablyteam.marriagelab.loader.file;

import lombok.RequiredArgsConstructor;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.utils.Configuration;

@RequiredArgsConstructor
public class FileLoader implements Loader {

    private final MarriageLab plugin;

    @Override
    public void load() {
        plugin.setConfig(new Configuration(plugin, "config"));
        plugin.setLanguage(new Configuration(plugin, "language"));
    }
}
