package org.lablyteam.marriagelab;

import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.lablyteam.marriagelab.storage.StorageMethod;
import org.lablyteam.marriagelab.storage.database.Database;
import org.lablyteam.marriagelab.storage.database.mongo.MongoDatabase;
import org.lablyteam.marriagelab.storage.manager.DataManager;
import org.lablyteam.marriagelab.storage.manager.mongo.MongoDataManager;
import org.lablyteam.marriagelab.storage.manager.yaml.YamlDataManager;
import org.lablyteam.marriagelab.storage.model.User;
import org.lablyteam.marriagelab.utils.Configuration;

public class MarriageLab extends JavaPlugin {

    @Getter
    private Configuration config;
    @Getter
    private DataManager<User> dataManager;

    @Override
    public void onEnable() {
        setupConfiguration();
        setupStorage();

        getLogger().info("MarriageLab version " + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MarriageLab version " + getDescription().getVersion() + " has been disabled!");
        getLogger().info("(see you next time...)");
    }

    private void setupConfiguration() {
        this.config = new Configuration(this, "config");
    }

    private void setupStorage() {
        StorageMethod storageMethod = StorageMethod.valueOf(config.getString("config.storage.method"));
        switch(storageMethod) {
            case MONGODB: {
                getLogger().info("Using MongoDB as the storage method.");

                MongoDatabase database = new MongoDatabase(
                        config.getString("config.storage.mongodb.hostname"),
                        config.getInt("config.storage.mongodb.port"),
                        config.getString("config.storage.mongodb.username"),
                        config.getString("config.storage.mongodb.password"),
                        config.getString("config.storage.mongodb.database"),
                        config.getBoolean("config.storage.mongodb.ssl")
                );

                Datastore datastore = Morphia.createDatastore(
                        database.getClient(),
                        config.getString("config.storage.mongodb.database")
                );

                datastore.getMapper().setOptions(
                        MapperOptions.builder().classLoader(
                                MarriageLab.class.getClassLoader()
                        ).build()
                );

                datastore.ensureIndexes();

                this.dataManager = new MongoDataManager<>(User.class, datastore);
                return;
            }
            case YAML: {
                getLogger().warning("Using YAML as the storage method. This is not recommended as it contains a lot of bugs.");
                this.dataManager = new YamlDataManager<>(User.class, new Configuration(this, "data"));
                return;
            }
        }
    }
}
