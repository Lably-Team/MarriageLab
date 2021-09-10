package org.lablyteam.marriagelab;

import org.bukkit.plugin.java.JavaPlugin;
import org.lablyteam.marriagelab.loader.Loader;
import org.lablyteam.marriagelab.loader.main.MainLoader;
import org.lablyteam.marriagelab.manager.RequestManager;
import org.lablyteam.marriagelab.storage.database.Database;
import org.lablyteam.marriagelab.storage.manager.DataManager;
import org.lablyteam.marriagelab.storage.model.User;
import org.lablyteam.marriagelab.utils.Configuration;

public class MarriageLab extends JavaPlugin {

    private Database database;
    private Configuration config, language;
    private DataManager<User> dataManager;
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

    public Database getPluginDatabase() {
        return this.database;
    }

    public void setPluginDatabase(Database database) {
        this.database = database;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setLanguage(Configuration language) {
        this.language = language;
    }

    public void setDataManager(DataManager<User> dataManager) {
        this.dataManager = dataManager;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }
}
