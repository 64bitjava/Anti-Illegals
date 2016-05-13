package me.NickNorkus.aicore.commands;

import org.bukkit.plugin.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class VirtualCommand implements Listener
{
    private Plugin plugin;
    private TreeMap<String, SubCommand> subCommands;
    private String command;

    public VirtualCommand(final Plugin plugin, final String command) {
        this.plugin = null;
        this.subCommands = new TreeMap<String, SubCommand>();
        this.command = "";
        this.plugin = plugin;
        this.command = command;
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public SubCommand getSubCommand(final String command) {
        if (this.subCommands.containsKey(command.toLowerCase())) {
            return this.subCommands.get(command.toLowerCase());
        }
        return null;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    @EventHandler
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();
        final String[] data = message.split(" ");
        final String command = data[0].replace("/", "").toLowerCase();
        String[] args = new String[0];
        if (data.length > 1) {
            args = new String[data.length - 1];
            for (int i = 1; i < data.length; ++i) {
                args[i - 1] = data[i];
            }
        }
        if (!command.equalsIgnoreCase(this.getCommand())) {
            return;
        }
        event.setCancelled(true);
        this.onCommand(player, command, args);
    }

    public void onCommand(final Player player, final String command, final String[] args) {
    }
}
