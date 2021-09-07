package org.lablyteam.marriagelab.storage.manager.yaml;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.lablyteam.marriagelab.storage.manager.DataManager;
import org.lablyteam.marriagelab.storage.model.User;
import org.lablyteam.marriagelab.utils.Configuration;

import java.util.UUID;

public class YamlDataManager<T extends ConfigurationSerializable> implements DataManager<T> {

    private final Class<T> type;
    private final Configuration file;

    public YamlDataManager(Class<T> type, Configuration file) {
        this.type = type;
        this.file = file;
    }

    @Override
    public T find(UUID uuid) {
        String path = type.getName().toLowerCase() + "." + uuid.toString();
        if(!file.contains(path)) {
            return null;
        }

        return type.cast(file.get(path + uuid.toString()));
    }

    @Override
    public boolean contains(UUID uuid) {
        String path = type.getName().toLowerCase() + "." + uuid.toString();
        return file.contains(path);
    }

    @Override
    public void save(UUID uuid, T object) {
        String path = type.getName().toLowerCase() + "." + uuid.toString();
        file.set(path, object);
        file.save();
    }

    @Override
    public void remove(UUID uuid) {
        String path = type.getName().toLowerCase() + "." + uuid.toString();
        file.set(path, null);
        file.save();
    }
}
