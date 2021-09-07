package org.lablyteam.marriagelab.storage.manager;

import java.util.UUID;

public interface DataManager<T> {

    T find(UUID uuid);

    boolean contains(UUID uuid);

    void save(UUID uuid, T object);

    void remove(UUID uuid);
}
