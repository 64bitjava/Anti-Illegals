package me.NickNorkus.aicore.commands;

import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import me.NickNorkus.aicore.*;
import org.bukkit.entity.*;
import me.NickNorkus.aicore.lang.*;
import java.util.*;
import org.bukkit.command.*;
import me.NickNorkus.aicore.ui.*;

public abstract class BaseCommand implements CommandExecutor
{
    private Plugin plugin;
    private TreeMap<String, SubCommand> subCommands;
    private static TreeMap<String, BaseCommand> baseCommands;
    private String command;
    private boolean arguments;
    private String permission;

    static {
        BaseCommand.baseCommands = new TreeMap<String, BaseCommand>();
    }

    public BaseCommand(final Plugin plugin, final String command, final String permission) {
        this.plugin = null;
        this.subCommands = new TreeMap<String, SubCommand>();
        this.command = "";
        this.arguments = false;
        this.permission = "";
        SendConsole.info("Adding command '" + command + "'");
        this.setCommand(command);
        this.setPermission(permission);
        this.plugin = plugin;
        addBaseCommand(this);
    }

    public BaseCommand(final Plugin plugin, final String command) {
        this.plugin = null;
        this.subCommands = new TreeMap<String, SubCommand>();
        this.command = "";
        this.arguments = false;
        this.permission = "";
        SendConsole.info("Adding command '" + command + "'");
        this.setCommand(command);
        this.plugin = plugin;
        addBaseCommand(this);
    }

    public BaseCommand(final Plugin plugin) {
        this.plugin = null;
        this.subCommands = new TreeMap<String, SubCommand>();
        this.command = "";
        this.arguments = false;
        this.permission = "";
        this.plugin = plugin;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(final String permission) {
        this.permission = permission;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean hasArguments() {
        return this.arguments;
    }

    public void setArguments(final boolean arguments) {
        this.arguments = arguments;
    }

    public static TreeMap<String, BaseCommand> getBaseCommands() {
        return BaseCommand.baseCommands;
    }

    public List<String> getAliases() {
        return (List<String>)((JavaPlugin)BasePlugin.getInstance()).getCommand(this.getCommand()).getAliases();
    }

    public static List<String> getAliases(final String command) {
        return (List<String>)((JavaPlugin)BasePlugin.getInstance()).getCommand(command).getAliases();
    }

    public static void addBaseCommand(final BaseCommand command) {
        BaseCommand.baseCommands.put(command.getCommand(), command);
        ((JavaPlugin)BasePlugin.getInstance()).getCommand(command.getCommand()).setExecutor((CommandExecutor)command);
    }

    public void addSubCommand(final SubCommand command) {
        command.setBaseCommand(this);
        this.subCommands.put(command.getCommand(), command);
    }

    public Player getPlayer(final Object sender) {
        if (sender instanceof Player) {
            return (Player)sender;
        }
        return null;
    }

    public SubCommand getSubCommand(final String command) {
        SubCommand scmd = null;
        if (this.subCommands.containsKey("*")) {
            scmd = this.subCommands.get("*");
        }
        if (this.subCommands.containsKey(command.toLowerCase())) {
            scmd = this.subCommands.get(command.toLowerCase());
        }
        return scmd;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public List<SubCommand> getSubCommands() {
        return new ArrayList<SubCommand>(this.subCommands.values());
    }

    public void showHelp(final String command, final Object sender) {
        for (final SubCommand cmd : this.getSubCommands()) {
            String message = Messages.getString("help-commandline");
            message = message.replace("{COMMAND}", command);
            message = message.replace("{SUBCOMMAND}", cmd.getCommand());
        }
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player player = this.getPlayer(sender);
        if (player != null && this.permission != "" && !player.hasPermission(this.getPermission())) {
            final String message = Messages.getString("no-permission");
            SendGame.toPlayer((message == null) ? "&cYou do not have permission!" : message, player);
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        final SubCommand subCommand = this.getSubCommand(args[0]);
        if (subCommand != null) {
            subCommand.onCommand(sender, args);
            return true;
        }
        if (this.hasArguments()) {
            return true;
        }
        SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + Messages.getString("invalid-arguments"), sender);
        return false;
    }
}
