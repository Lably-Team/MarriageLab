package org.lablyteam.marriagelab.loader.storage;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.RequiredArgsConstructor;
import org.lablyteam.marriagelab.MarriageLab;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.storage.StorageMethod;
import org.lablyteam.marriagelab.storage.database.Database;
import org.lablyteam.marriagelab.storage.database.mongo.MongoDatabase;
import org.lablyteam.marriagelab.storage.manager.DataManager;
import org.lablyteam.marriagelab.storage.manager.mongo.MongoDataManager;
import org.lablyteam.marriagelab.storage.manager.yaml.YamlDataManager;
import org.lablyteam.marriagelab.storage.model.User;
import org.lablyteam.marriagelab.utils.Configuration;

@RequiredArgsConstructor
public class StorageLoader implements Loader {

    private final MarriageLab plugin;

    @Override
    public void load() {
        StorageMethod storageMethod = StorageMethod.valueOf(plugin.getConfig().getString("config.storage.method"));
        switch(storageMethod) {
            case MONGODB: {
                plugin.getLogger().info("Using MongoDB as the storage method.");

                MongoDatabase database = new MongoDatabase(
                        plugin.getConfig().getString("config.storage.mongodb.hostname"),
                        plugin.getConfig().getInt("config.storage.mongodb.port"),
                        plugin.getConfig().getString("config.storage.mongodb.username"),
                        plugin.getConfig().getString("config.storage.mongodb.password"),
                        plugin.getConfig().getString("config.storage.mongodb.database"),
                        plugin.getConfig().getBoolean("config.storage.mongodb.ssl")
                );

                Datastore datastore = Morphia.createDatastore(
                        database.getClient(),
                        plugin.getConfig().getString("config.storage.mongodb.database")
                );

                // TODO: change this since it's marked for removal
                datastore.getMapper().setOptions(
                        MapperOptions.builder().classLoader(
                                MarriageLab.class.getClassLoader()
                        ).build()
                );

                datastore.ensureIndexes();

                DataManager<User> dataManager = new MongoDataManager<>(User.class, datastore);
                plugin.setDatabase(database);
                plugin.setDataManager(dataManager);
                return;
            }
            case YAML: {
                plugin.getLogger().info("Using YAML as the storage method.");

                DataManager<User> dataManager = new YamlDataManager<>(
                        User.class,
                        new Configuration(plugin, "data")
                );

                plugin.setDataManager(dataManager);
            }
        }
    }

    @Override
    public void unload() {
        Database database = plugin.getDatabase();
        if(database != null) {
            database.disconnect();
        }
    }
}
