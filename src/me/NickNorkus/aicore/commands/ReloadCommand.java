package me.NickNorkus.aicore.commands;

import me.NickNorkus.aicore.*;
import me.NickNorkus.aicore.lang.*;
import me.NickNorkus.aicore.ui.*;

public class ReloadCommand extends SubCommand
{
    public ReloadCommand(final String command, final String permission) {
        super(command, permission);
    }

    @Override
    public boolean onCommand(final Object sender, final String[] args) {
        if (!super.onCommand(sender, args)) {
            return false;
        }
        try {
            ((BasePlugin)this.getBaseCommand().getPlugin()).loadConfiguration();
            Messages.reload();
            SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + Messages.getString("plugin-reloaded"), sender);
        }
        catch (Exception ex) {}
        return true;
    }
}
