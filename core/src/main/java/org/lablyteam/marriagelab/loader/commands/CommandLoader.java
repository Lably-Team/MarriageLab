package org.lablyteam.marriagelab.loader.commands;

import lombok.RequiredArgsConstructor;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.fixeddev.commandflow.command.Command;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.commands.usage.CommandUsageBuilder;
import org.lablyteam.marriagelab.loader.Loader;

import java.util.List;

@RequiredArgsConstructor
public class CommandLoader implements Loader {

    private final MarriageLab plugin;
    private AnnotatedCommandTreeBuilder builder;
    private CommandManager commandManager;

    @Override
    public void load() {
        this.commandManager = new BukkitCommandManager("MarriageLab");
        this.commandManager.setUsageBuilder(new CommandUsageBuilder(plugin));

        PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());

        this.builder = new AnnotatedCommandTreeBuilderImpl(injector);
    }

    public void registerCommand(CommandClass... commandClasses) {
        for (CommandClass commandClass : commandClasses) {
            List<Command> command = builder.fromClass(commandClass);
            commandManager.registerCommands(command);
        }
    }
}
