package org.lablyteam.marriagelab.commands.usage;

public enum CommandUsage {

    MARRY("/marry <player>"),
    PARTNERSHIP("/partnership <option> [<player>]");

    private final String usage;

    CommandUsage(String usage){
        this.usage = usage;
    }

    public String getUsage(){
        return usage;
    }
}
