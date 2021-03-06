package me.NickNorkus.aicore.ui;

import org.bukkit.entity.*;
import me.NickNorkus.aicore.utils.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import java.io.*;

public class SendGame
{
    public static void toPlayer(String message, final Player player) {
        message = ColorUtils.toColors(message);
        player.sendMessage(message);
    }

    public static void toServer(String message) {
        message = ColorUtils.toColors(message);
        Bukkit.broadcastMessage(message);
    }

    public static void toAllServers(final Plugin plugin, final String message, final String channel) {
        final String data = ColorUtils.toColors(message);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                final ByteArrayOutputStream b = new ByteArrayOutputStream();
                final DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Broadcast");
                    out.writeUTF(data);
                    Bukkit.getServer().sendPluginMessage(plugin, channel, b.toByteArray());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
