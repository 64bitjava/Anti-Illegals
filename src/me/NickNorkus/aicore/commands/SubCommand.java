package me.NickNorkus.aicore.commands;

import org.bukkit.entity.*;
import me.NickNorkus.aicore.lang.*;
import me.NickNorkus.aicore.ui.*;

public class SubCommand
{
    private String description;
    private String usage;
    private String usageFormat;
    private String descriptionFormat;
    private String command;
    private String permission;
    private BaseCommand baseCommand;

    public SubCommand(final String command, final String permission, final String description) {
        this.description = "";
        this.usage = "";
        this.usageFormat = "";
        this.descriptionFormat = "";
        this.command = "";
        this.permission = "";
        this.baseCommand = null;
        this.setCommand(command);
        this.setPermission(permission);
        this.setDescription(description);
    }

    public SubCommand(final String command, final String permission) {
        this.description = "";
        this.usage = "";
        this.usageFormat = "";
        this.descriptionFormat = "";
        this.command = "";
        this.permission = "";
        this.baseCommand = null;
        this.setCommand(command);
        this.setPermission(permission);
    }

    public BaseCommand getBaseCommand() {
        return this.baseCommand;
    }

    public void setBaseCommand(final BaseCommand command) {
        this.baseCommand = command;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(final String permission) {
        this.permission = permission;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public String getUsageFormat() {
        return this.usageFormat;
    }

    public String getDescriptionFormat() {
        return this.descriptionFormat;
    }

    public void setUsageFormat(final String format) {
        this.usageFormat = format;
    }

    public void setDescriptionFormat(final String format) {
        this.descriptionFormat = format;
    }

    public String getUsage() {
        return this.usage;
    }

    public void setUsage(final String usage) {
        this.usage = usage;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Player getPlayer(final Object sender) {
        if (sender instanceof Player) {
            return (Player)sender;
        }
        return null;
    }

    public boolean onCommand(final Object sender, final String[] args) {
        final Player player = this.getPlayer(sender);
        if (player != null && !player.hasPermission(this.getPermission())) {
            final String message = Messages.getString("no-permission");
            SendGame.toPlayer((message == null) ? "&cYou do not have permission!" : message, player);
            return false;
        }
        return true;
    }
}
