package org.lablyteam.marriagelab.storage.manager.mongo;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import org.lablyteam.marriagelab.storage.manager.DataManager;

import java.util.UUID;

import static dev.morphia.query.experimental.filters.Filters.eq;

public class MongoDataManager<T> implements DataManager<T> {

    private final Class<T> type;
    private final Datastore datastore;

    public MongoDataManager(Class<T> type, Datastore datastore) {
        this.type = type;
        this.datastore = datastore;
    }

    @Override
    public T find(UUID uuid) {
        return query(uuid).first();
    }

    @Override
    public boolean contains(UUID uuid) {
        return find(uuid) != null;
    }

    // TODO: add an interface since we don't need the UUID on the mongo data manager
    @Override
    public void save(UUID uuid, T object) {
        datastore.save(object);
    }

    @Override
    public void remove(UUID uuid) {
        query(uuid).findAndDelete();
    }

    private Query<T> query(UUID uuid) {
        return datastore.find(type).filter(eq("_id", uuid));
    }
}
