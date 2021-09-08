package org.lablyteam.marriagelab.commands.usage;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.usage.UsageBuilder;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.lablyteam.marriagelab.MarriageLab;

import java.util.ArrayList;
import java.util.List;

public class CommandUsageBuilder implements UsageBuilder {

    private final MarriageLab plugin;

    public CommandUsageBuilder(MarriageLab plugin) {
        this.plugin = plugin;
    }

    @Override
    public Component getUsage(CommandContext commandContext) {
        CommandUsage commandUsage;
        try {
            commandUsage = CommandUsage.valueOf(commandContext.getLabels().get(0).toUpperCase());
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }


        if (commandContext.getLabels().size() < 2) {
            return TextComponent.of(plugin.getLanguage().getString("language.usage")
                    .replace("%usage%", commandUsage.getUsage()));
        }

        List<String> arguments = new ArrayList<>(commandContext.getLabels());
        arguments.remove(0);

        String usage = commandUsage.getUsage()
                .replace("%arg%", String.join(" ", arguments));

        return TextComponent.of(plugin.getLanguage().getString("language.usage")
                .replace("%usage%", usage));
    }
}
