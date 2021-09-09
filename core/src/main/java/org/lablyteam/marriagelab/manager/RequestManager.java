package org.lablyteam.marriagelab.manager;

import java.util.UUID;

public interface RequestManager {

    boolean hasPendingRequests(UUID from);
    void addRequest(UUID from, UUID to);
    void cancelRequest(UUID from);

    void acceptRequest(UUID from);
    void denyRequest(UUID from);
}
