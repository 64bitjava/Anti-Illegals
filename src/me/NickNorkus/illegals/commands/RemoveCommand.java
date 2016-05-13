package me.NickNorkus.illegals.commands;

import me.NickNorkus.aicore.commands.*;
import me.NickNorkus.illegals.*;
import me.NickNorkus.aicore.lang.*;
import me.NickNorkus.aicore.ui.*;
import java.util.*;

public class RemoveCommand extends SubCommand
{
    public RemoveCommand(final String command, final String permission, final String description) {
        super(command, permission, description);
    }

    @Override
    public boolean onCommand(final Object sender, final String[] args) {
        if (!super.onCommand(sender, args)) {
            return false;
        }
        final Map<String, Integer> found = ((Main)this.getBaseCommand().getPlugin()).checkPlayers(true);
        SendList.toSender(found, sender, String.valueOf(Messages.getString("prefix")) + Messages.getString("removed-header"), Messages.getString("removed-line"), 1);
        return true;
    }
}
