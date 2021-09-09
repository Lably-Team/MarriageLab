package org.lablyteam.marriagelab.manager;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface RequestManager {

    boolean hasPendingRequests(UUID from);
    void addRequest(Player from, Player to);
    void addRequest(UUID from, UUID to);
    void cancelRequest(UUID from);

    UUID getRequest(UUID from);

    void acceptRequest(UUID from);
    void denyRequest(UUID from);
}
