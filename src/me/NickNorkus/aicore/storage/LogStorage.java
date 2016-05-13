package me.NickNorkus.aicore.storage;

import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import me.NickNorkus.aicore.ui.*;
import java.io.*;

public class LogStorage
{
    private Plugin plugin;
    private String logName;
    String folder;
    private YamlConfiguration pluginConfig;
    private File logFile;
    private boolean loaded;

    public LogStorage(final Plugin plugin, final String logName) {
        this.plugin = null;
        this.logName = "";
        this.folder = "";
        this.loaded = false;
        this.plugin = plugin;
        this.logName = logName;
        this.loadConfig();
    }

    public LogStorage(final Plugin plugin, final String folder, final String logName) {
        this.plugin = null;
        this.logName = "";
        this.folder = "";
        this.loaded = false;
        this.plugin = plugin;
        this.folder = folder;
        this.logName = logName;
        this.loadConfig();
    }

    public YamlConfiguration getConfig() {
        if (!this.loaded) {
            this.loadConfig();
        }
        return this.pluginConfig;
    }

    public String getContents() {
        try {
            final FileInputStream fis = new FileInputStream(this.logFile);
            final byte[] data = new byte[(int)this.logFile.length()];
            fis.read(data);
            fis.close();
            final String s = new String(data, "UTF-8");
            return s;
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public File getLogFile() {
        return this.logFile;
    }

    public void appendLog(final String message) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    final FileWriter fw = new FileWriter(LogStorage.this.logFile, true);
                    fw.write(String.valueOf(message) + "\n");
                    fw.close();
                }
                catch (Exception ex) {}
            }
        });
    }

    public void loadConfig() {
        this.logFile = new File(this.plugin.getDataFolder() + "/" + this.folder, String.valueOf(this.logName) + ".log");
        if (this.logFile.exists()) {
            this.loaded = true;
        }
        else {
            try {
                this.plugin.getDataFolder().mkdir();
                new File(this.plugin.getDataFolder() + "/" + this.folder).mkdir();
                final InputStream jarURL = this.plugin.getClass().getResourceAsStream("/" + this.logName + ".log");
                if (jarURL != null) {
                    SendConsole.info("Copying '" + this.logFile + ".log' from the resources!");
                    copyFile(jarURL, this.logFile);
                }
                else {
                    SendConsole.info("Creating new file '" + this.logFile + "'!");
                    this.logFile.createNewFile();
                }
                this.loaded = true;
            }
            catch (Exception ex) {}
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
