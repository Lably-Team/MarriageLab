package org.lablyteam.marriagelab;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.lablyteam.marriagelab.gender.Gender;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.loader.main.MainLoader;
import org.lablyteam.marriagelab.manager.RequestManager;
import org.lablyteam.marriagelab.manager.RequestManagerImpl;
import org.lablyteam.marriagelab.storage.StorageMethod;
import org.lablyteam.marriagelab.storage.database.Database;
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

    @Setter @Getter
    private Database database;
    @Setter
    private Configuration config, language;
    @Setter
    private DataManager<User> dataManager;
    @Setter
    private RequestManager requestManager;
    private Loader loader;

    @Override
    public void onEnable() {
        this.loader = new MainLoader(this);
        loader.load();

        getLogger().info("MarriageLab version " + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        loader.unload();

        getLogger().info("MarriageLab version " + getDescription().getVersion() + " has been disabled!");
        getLogger().info("(see you next time...)");
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

    @Deprecated(forRemoval = true)
    public void reload() {
        this.config = new Configuration(this, "config");
        this.language = new Configuration(this, "language");
    }

    public RequestManager getRequestManager() {
        return this.requestManager;
    }
}
