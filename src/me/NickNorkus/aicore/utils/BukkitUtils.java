package me.NickNorkus.aicore.utils;

import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.*;

import java.util.Collection;

public class BukkitUtils
{
    public static String getServerName() {
        return Bukkit.getServerName();
    }

    public static int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    public static Plugin[] getPlugins() {
        return Bukkit.getPluginManager().getPlugins();
    }

    public static int getPort() {
        return Bukkit.getPort();
    }

    public static int getPluginCount() {
        return Bukkit.getPluginManager().getPlugins().length;
    }

    public static String getVersion() {
        return Bukkit.getBukkitVersion();
    }

    public static Collection<? extends Player> getOnlinePlayerCount() {
        return Bukkit.getOnlinePlayers();
    }

    public static int getTotalPlayerCount() {
        return Bukkit.getOfflinePlayers().length;
    }

    public static int getOPCount() {
        return Bukkit.getOperators().size();
    }

    public static Player[] getOperators() {
        return (Player[])Bukkit.getOperators().toArray();
    }

    public static String getIp() {
        final String bukkitIP = Bukkit.getServer().getIp();
        return (bukkitIP == null || bukkitIP == "") ? "127.0.0.1" : bukkitIP;
    }

    public static int getPlayerCount(final GameMode mode) {
        int size = 0;
        Collection<? extends Player> onlinePlayers;
        for (Collection<? extends Player> length = (onlinePlayers = Bukkit.getOnlinePlayers());
        int i = 0;i < length; ++i) {
            final Player p = (Player) onlinePlayers;
            if (p.getGameMode() == mode) {
                ++size;
            }
        }
        return size;
    }

    public static Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public static boolean hasWhitelist() {
        return Bukkit.hasWhitelist();
    }
}
