package me.NickNorkus.aicore.ui;

import org.bukkit.plugin.*;
import me.NickNorkus.aicore.storage.*;
import me.NickNorkus.aicore.config.*;
import me.NickNorkus.aicore.utils.*;
import java.util.*;
import java.text.*;
import org.bukkit.*;
import org.bukkit.command.*;

public class SendConsole
{
    static boolean enableLogging;
    static String prefix;
    static Plugin plugin;
    static LogStorage log;

    static {
        SendConsole.enableLogging = false;
        SendConsole.prefix = "[Nick] ";
        SendConsole.plugin = null;
        SendConsole.log = null;
    }

    public SendConsole(final Plugin plugin) {
        SendConsole.plugin = plugin;
        SendConsole.prefix = "[" + plugin.getName() + "] ";
        SendConsole.log = new LogStorage(plugin, "log");
    }

    public static LogStorage getLogStorage() {
        return SendConsole.log;
    }

    public static void debug(String message) {
        if (Configuration.debug) {
            message = ColorUtils.removeColors(message);
            Bukkit.getLogger().info(String.valueOf(SendConsole.prefix) + message);
        }
        if (Configuration.log) {
            try {
                final Date date = new Date();
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SendConsole.log.appendLog(String.valueOf(sdf.format(date)) + " [DEBUG] " + message);
            }
            catch (Exception ex) {}
        }
    }

    public static void message(final String message) {
        final ConsoleCommandSender console = SendConsole.plugin.getServer().getConsoleSender();
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        if (Configuration.log) {
            try {
                final Date date = new Date();
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SendConsole.log.appendLog(String.valueOf(sdf.format(date)) + " [MESSAGE] " + message);
            }
            catch (Exception ex) {}
        }
    }

    public static void info(String message) {
        message = ColorUtils.removeColors(message);
        Bukkit.getLogger().info(String.valueOf(SendConsole.prefix) + message);
        if (Configuration.log) {
            try {
                final Date date = new Date();
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SendConsole.log.appendLog(String.valueOf(sdf.format(date)) + " [INFO] " + message);
            }
            catch (Exception ex) {}
        }
    }

    public static void warning(String message) {
        message = ColorUtils.removeColors(message);
        Bukkit.getLogger().warning(String.valueOf(SendConsole.prefix) + message);
        if (Configuration.log) {
            try {
                final Date date = new Date();
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SendConsole.log.appendLog(String.valueOf(sdf.format(date)) + " [WARNING] " + message);
            }
            catch (Exception ex) {}
        }
    }

    public static void severe(String message) {
        message = ColorUtils.removeColors(message);
        Bukkit.getLogger().severe(String.valueOf(SendConsole.prefix) + message);
        if (Configuration.log) {
            try {
                final Date date = new Date();
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SendConsole.log.appendLog(String.valueOf(sdf.format(date)) + " [SEVERE] " + message);
            }
            catch (Exception ex) {}
        }
    }
}
