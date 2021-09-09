package org.lablyteam.marriagelab.internal;

import org.bukkit.command.CommandExecutor;

public class CommandClass {

    private final String name;
    private final CommandExecutor executor;

    public CommandClass(String name, CommandExecutor executor) {
        this.name = name;
        this.executor = executor;
    }

    public String getName() {
        return this.name;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }
}
