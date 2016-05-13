package me.NickNorkus.aicore.config;

import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import me.NickNorkus.aicore.ui.*;
import java.util.*;
import java.io.*;

public class Configuration
{
    public static boolean debug;
    public static boolean log;
    private static Plugin plugin;
    private static int configVersion;
    public static boolean firstStart;
    private static YamlConfiguration pluginConfig;
    private static File configFile;
    private static boolean loaded;

    static {
        Configuration.debug = false;
        Configuration.log = false;
        Configuration.plugin = null;
        Configuration.configVersion = 1;
        Configuration.firstStart = false;
        Configuration.loaded = false;
    }

    public Configuration(final Plugin plugin) {
        Configuration.plugin = plugin;
    }

    public Configuration(final Plugin plugin, final int config) {
        Configuration.plugin = plugin;
        loadConfig();
        if (Configuration.configVersion != -1 && Configuration.configVersion != getConfig().getInt("config")) {
            updateConfig();
        }
        Configuration.debug = getConfig().getBoolean("debug");
        Configuration.log = getConfig().getBoolean("log");
        Configuration.firstStart = getConfig().getBoolean("firstStart");
        if (Configuration.firstStart) {
            getConfig().set("firstStart", (Object)false);
        }
    }

    public static int getConfigVersion() {
        return Configuration.configVersion;
    }

    public static void setConfigVersion(final int version) {
        Configuration.configVersion = version;
    }

    public static YamlConfiguration getConfig() {
        if (!Configuration.loaded) {
            loadConfig();
        }
        return Configuration.pluginConfig;
    }

    public static File getConfigFile() {
        return Configuration.configFile;
    }

    public static void loadConfig() {
        Configuration.configFile = new File(Configuration.plugin.getDataFolder(), "config.yml");
        if (Configuration.configFile.exists()) {
            Configuration.pluginConfig = new YamlConfiguration();
            try {
                Configuration.pluginConfig.load(Configuration.configFile);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            Configuration.loaded = true;
        }
        else {
            try {
                Configuration.plugin.getDataFolder().mkdir();
                final InputStream jarURL = Configuration.plugin.getClass().getResourceAsStream("/config.yml");
                if (jarURL != null) {
                    SendConsole.info("Copying '" + Configuration.configFile + "' from the resources!");
                    copyFile(jarURL, Configuration.configFile);
                    (Configuration.pluginConfig = new YamlConfiguration()).load(Configuration.configFile);
                    Configuration.loaded = true;
                }
                else {
                    SendConsole.severe("Configuration file not found inside the plugin!");
                    SendConsole.severe("This error occurs when you forced a reload!");
                    Configuration.configVersion = -1;
                }
                jarURL.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateConfig() {
        SendConsole.warning("Updating configuration file!");
        final File backupConfig = new File(Configuration.plugin.getDataFolder(), "config_" + new Date().getTime() + ".yml");
        Configuration.configFile.renameTo(backupConfig);
        SendConsole.warning("Backup config saved to: " + backupConfig.getName());
        Configuration.configFile = null;
        loadConfig();
    }

    private static void copyFile(final InputStream in, final File out) throws Exception {
        final FileOutputStream fos = new FileOutputStream(out);
        try {
            final byte[] buf = new byte[1024];
            int i = 0;
            while ((i = in.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (in != null) {
                in.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        if (in != null) {
            in.close();
        }
        if (fos != null) {
            fos.close();
        }
    }
}
