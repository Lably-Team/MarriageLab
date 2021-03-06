package org.lablyteam.marriagelab.loader.commands;

import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.commands.MarriageLabCommand;
import org.lablyteam.marriagelab.commands.MarryCommand;
import org.lablyteam.marriagelab.internal.CommandClass;
import org.lablyteam.marriagelab.loader.Loader;

public class CommandLoader implements Loader {

    private final MarriageLab plugin;

    public CommandLoader(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        registerCommand(
                new CommandClass("marry", new MarryCommand(plugin)),
                new CommandClass("marriagelab", new MarriageLabCommand(plugin))
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
