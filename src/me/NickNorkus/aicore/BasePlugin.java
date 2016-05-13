package me.NickNorkus.aicore;

import org.bukkit.plugin.java.*;
import me.NickNorkus.aicore.ui.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import me.NickNorkus.aicore.config.*;

public abstract class BasePlugin extends JavaPlugin
{
    private static BasePlugin instance;
    private String version;

    static {
        BasePlugin.instance = null;
    }

    public BasePlugin() {
        this.version = "2016-05";
    }

    public void onEnable() {
        new SendConsole((Plugin)this);
        BasePlugin.instance = this;
        SendConsole.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        SendConsole.info("Plugin: " + this.getName() + " v" + this.getDescription().getVersion());
        SendConsole.info("Framework version: " + this.version);
        SendConsole.info("Author: Nick Norkus");
        SendConsole.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        this.loadConfiguration();
    }

    public void onDisable() {
        SendConsole.info("Cancelling all tasks ...");
        Bukkit.getScheduler().cancelAllTasks();
        super.onDisable();
    }

    public void initialize() {
        this.registerManagers();
        this.registerCommands();
        this.registerSchedulers();
        this.registerListeners(Bukkit.getPluginManager());
        this.registerChannels();
    }

    public static Plugin getInstance() {
        return (Plugin)BasePlugin.instance;
    }

    public void registerListeners(final PluginManager pm) {
    }

    public void registerSchedulers() {
    }

    public void registerCommands() {
    }

    public void registerManagers() {
    }

    public void registerChannels() {
    }

    public void loadDependencies(final PluginManager pm) {
    }

    public void loadConfiguration() {
        try {
            new Configuration((Plugin)this, Configuration.getConfigVersion());
            Configuration.loadConfig();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
