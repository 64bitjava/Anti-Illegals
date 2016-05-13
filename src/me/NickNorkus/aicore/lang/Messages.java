package me.NickNorkus.aicore.lang;

import org.bukkit.plugin.*;
import me.NickNorkus.aicore.storage.*;
import org.bukkit.configuration.file.*;
import java.util.*;
import org.bukkit.*;
import java.io.*;

public class Messages
{
    private String language;
    private Plugin plugin;
    private static YamlStorage languageFile;
    private static TreeMap<String, Object> defaultPhrases;

    static {
        Messages.languageFile = null;
        Messages.defaultPhrases = new TreeMap<String, Object>();
    }

    public Messages(final Plugin plugin, final String language) {
        this.language = "en";
        this.plugin = null;
        this.setLanguage(language);
        this.setPlugin(plugin);
        Messages.languageFile = new YamlStorage(plugin, "lang_" + language);
    }

    public static String getString(final String message) {
        final YamlConfiguration config = Messages.languageFile.getConfig();
        final String data = config.getString(message);
        return data;
    }

    public static List<String> getStringList(final String message) {
        final YamlConfiguration config = Messages.languageFile.getConfig();
        final List<String> data = (List<String>)config.getStringList(message);
        return data;
    }

    public static void reload() {
        Messages.languageFile.loadConfig();
    }

    public void set(final String key, final Object message) {
        Messages.defaultPhrases.put(key, message);
        Bukkit.getScheduler().runTask(this.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                final YamlConfiguration config = Messages.languageFile.getConfig();
                final Object data = config.get(key);
                if (data == null) {
                    config.set(key, message);
                    try {
                        config.save(Messages.languageFile.getConfigFile());
                    }
                    catch (IOException ex) {}
                }
            }
        });
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public void setPlugin(final Plugin plugin) {
        this.plugin = plugin;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }
}
