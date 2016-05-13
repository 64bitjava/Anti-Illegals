package me.NickNorkus.illegals.commands;

import org.bukkit.plugin.*;
import me.NickNorkus.aicore.lang.*;
import me.NickNorkus.aicore.commands.*;

public class aiCommand extends BaseCommand
{
    public aiCommand(final Plugin plugin, final String command, final String permission) {
        super(plugin, command, permission);
        this.addSubCommand(new ReloadCommand("reload", "illegals.reload"));
        final HelpCommand help = new HelpCommand("help", "illegals.help", "", Messages.getStringList("help"));
        this.addSubCommand(help);
        this.addSubCommand(new CheckCommand("check", "illegals.check", ""));
        this.addSubCommand(new RemoveCommand("remove", "illegals.remove", ""));
        this.addSubCommand(new EffectsCommand("effects", "illegals.effects", ""));
        this.addSubCommand(new EffectsCommand("effect", "illegals.effects", ""));
    }
}


