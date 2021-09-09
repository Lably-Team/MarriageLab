package org.lablyteam.marriagelab.loader.commands;

import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.commands.MarryCommand;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.internal.CommandClass;

public class CommandLoader implements Loader {

    private final MarriageLab plugin;

    public CommandLoader(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        registerCommand(
                new CommandClass("marry", new MarryCommand(plugin))
        );
    }

    private void registerCommand(CommandClass... commands) {
        for(CommandClass command : commands) {
            plugin.getCommand(
                    command.getName()
            ).setExecutor(
                    command.getExecutor()
            );
        }
    }
}
