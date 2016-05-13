package me.NickNorkus.aicore.storage;

import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import me.NickNorkus.aicore.ui.*;
import org.bukkit.configuration.*;
import java.io.*;

public class YamlStorage
{
    private Plugin plugin;
    private String configName;
    String folder;
    private YamlConfiguration pluginConfig;
    private File configFile;
    private boolean loaded;

    public YamlStorage(final Plugin plugin, final String configName) {
        this.plugin = null;
        this.configName = "";
        this.folder = "";
        this.loaded = false;
        this.plugin = plugin;
        this.configName = configName;
        this.loadConfig();
    }

    public YamlStorage(final Plugin plugin, final String folder, final String configName) {
        this.plugin = null;
        this.configName = "";
        this.folder = "";
        this.loaded = false;
        this.plugin = plugin;
        this.folder = folder;
        this.configName = configName;
        this.loadConfig();
    }

    public YamlConfiguration getConfig() {
        if (!this.loaded) {
            this.loadConfig();
        }
        return this.pluginConfig;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public void loadConfig() {
        this.configFile = new File(this.plugin.getDataFolder() + "/" + this.folder, String.valueOf(this.configName) + ".yml");
        if (this.configFile.exists()) {
            this.pluginConfig = new YamlConfiguration();
            try {
                this.pluginConfig.load(this.configFile);
            }
            catch (FileNotFoundException ex2) {
                SendConsole.severe("Error in [" + this.configName + "] - File not found!");
            }
            catch (IOException ex3) {
                SendConsole.severe("Error in [" + this.configName + "] - Error while reading!");
            }
            catch (InvalidConfigurationException ex) {
                SendConsole.severe("Error in [" + this.configName + "] - Corrupted YML!");
                ex.printStackTrace();
            }
            this.loaded = true;
        }
        else {
            try {
                this.plugin.getDataFolder().mkdir();
                new File(this.plugin.getDataFolder() + "/" + this.folder).mkdir();
                final InputStream jarURL = this.plugin.getClass().getResourceAsStream("/" + this.configName + ".yml");
                if (jarURL != null) {
                    SendConsole.info("Copying '" + this.configFile + ".yml' from the resources!");
                    copyFile(jarURL, this.configFile);
                }
                else {
                    SendConsole.info("Creating new file '" + this.configFile + "'!");
                    this.configFile.createNewFile();
                }
                (this.pluginConfig = new YamlConfiguration()).load(this.configFile);
                this.loaded = true;
            }
            catch (Exception ex4) {}
        }
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
