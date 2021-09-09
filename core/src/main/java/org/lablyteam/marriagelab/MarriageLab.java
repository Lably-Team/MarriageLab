package org.lablyteam.marriagelab;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.lablyteam.marriagelab.gender.Gender;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.loader.main.MainLoader;
import org.lablyteam.marriagelab.manager.RequestManager;
import org.lablyteam.marriagelab.manager.RequestManagerImpl;
import org.lablyteam.marriagelab.storage.StorageMethod;
import org.lablyteam.marriagelab.storage.database.mongo.MongoDatabase;
import org.lablyteam.marriagelab.storage.manager.DataManager;
import org.lablyteam.marriagelab.storage.manager.mongo.MongoDataManager;
import org.lablyteam.marriagelab.storage.manager.yaml.YamlDataManager;
import org.lablyteam.marriagelab.storage.model.User;
import org.lablyteam.marriagelab.utils.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MarriageLab extends JavaPlugin {

    private Configuration config, language;
    private DataManager<User> dataManager;
    private RequestManager requestManager;
    private Loader loader;

    @Override
    public void onEnable() {
        this.loader = new MainLoader(this);

        setupConfiguration();
        setupStorage();
        setupRequestManager();

        loader.load();

        getLogger().info("MarriageLab version " + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        loader.unload();

        getLogger().info("MarriageLab version " + getDescription().getVersion() + " has been disabled!");
        getLogger().info("(see you next time...)");
    }

    private void setupRequestManager() {
        this.requestManager = new RequestManagerImpl(this);
    }

    private void setupConfiguration() {
        this.config = new Configuration(this, "config");
        this.language = new Configuration(this, "language");
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

                // TODO: change this since it's marked for removal
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
                getLogger().info("Using YAML as the storage method.");
                this.dataManager = new YamlDataManager<>(User.class, new Configuration(this, "data"));
                return;
            }
        }
    }

    public Configuration getConfig() {
        return this.config;
    }

    public Configuration getLanguage() {
        return this.language;
    }

    public DataManager<User> getDataManager() {
        return this.dataManager;
    }

    public void reload() {
        this.config = new Configuration(this, "config");
        this.language = new Configuration(this, "language");
    }

    public RequestManager getRequestManager() {
        return this.requestManager;
    }
}
