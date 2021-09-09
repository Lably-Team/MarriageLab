package org.lablyteam.marriagelab.internal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandExecutor;

@RequiredArgsConstructor @Getter @Setter
public class CommandClass {

    private final String name;
    private final CommandExecutor executor;
}
