package org.lablyteam.marriagelab.commands.usage;

public enum CommandUsage {

    REQUEST("/marry request <player>"),
    GENDER("/marry gender <gender>");

    private final String usage;

    CommandUsage(String usage){
        this.usage = usage;
    }

    public String getUsage(){
        return usage;
    }
}
