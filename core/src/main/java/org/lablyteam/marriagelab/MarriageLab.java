package org.lablyteam.marriagelab;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.lablyteam.marriagelab.gender.Gender;
import org.lablyteam.marriagelab.storage.StorageMethod;
import org.lablyteam.marriagelab.storage.database.Database;
import org.lablyteam.marriagelab.storage.database.mongo.MongoDatabase;
import org.lablyteam.marriagelab.storage.manager.DataManager;
import org.lablyteam.marriagelab.storage.manager.mongo.MongoDataManager;
import org.lablyteam.marriagelab.storage.manager.yaml.YamlDataManager;
import org.lablyteam.marriagelab.storage.model.User;
import org.lablyteam.marriagelab.utils.Configuration;

import java.util.UUID;

public class MarriageLab extends JavaPlugin {

    private Database database;
    @Getter
    private Configuration config;
    @Getter
    private DataManager<User> dataManager;

    @Override
    public void onEnable() {
        setupConfiguration();
        setupStorage();

        getLogger().info("MarriageLab version " + getDescription().getVersion() + " has been enabled!");

        testStorage();
    }

    @Override
    public void onDisable() {
        database.disconnect();

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

                this.database = new MongoDatabase(
                        config.getString("config.storage.mongodb.hostname"),
                        config.getInt("config.storage.mongodb.port"),
                        config.getString("config.storage.mongodb.username"),
                        config.getString("config.storage.mongodb.password"),
                        config.getString("config.storage.mongodb.database")
                );

                Datastore datastore = Morphia.createDatastore(config.getString("config.storage.mongodb.database"));

                datastore.getMapper().setOptions(
                        MapperOptions.builder().classLoader(
                                MarriageLab.class.getClassLoader()
                        ).build()
                );

                datastore.ensureIndexes();

                this.dataManager = new MongoDataManager<>(User.class, datastore);
                break;
            }
            case YAML: {
                getLogger().info("Using YAML as the storage method.");
                this.dataManager = new YamlDataManager<>(User.class, new Configuration(this, "data"));
                break;
            }
        }
    }

    private void testStorage() {
        User user = new User(UUID.randomUUID(), Gender.FEMALE, "nuko");
        dataManager.save(user.getUUID(), user);
    }
}
