package me.NickNorkus.illegals.commands;

import me.NickNorkus.aicore.commands.*;
import me.NickNorkus.aicore.storage.SendConsole;
import me.NickNorkus.aicore.ui.SendList;
import me.NickNorkus.aicore.ui.SendUnknown;
import me.NickNorkus.illegals.*;
import me.NickNorkus.aicore.lang.*;
import java.util.*;
import me.NickNorkus.aicore.utils.*;

public class CheckCommand extends SubCommand
{
    public CheckCommand(final String command, final String permission, final String description) {
        super(command, permission, description);
    }

    @Override
    public boolean onCommand(final Object sender, final String[] args) {
        if (!super.onCommand(sender, args)) {
            return false;
        }
        if (args.length < 2 || !args[1].equalsIgnoreCase("pv")) {
            final Map<String, Integer> found = ((Main)this.getBaseCommand().getPlugin()).checkPlayers(false);
            SendList.toSender(found, sender, String.valueOf(Messages.getString("prefix")) + Messages.getString("check-header"), Messages.getString("check-line"), 1);
            return true;
        }
        if (!Permissions.hasPermissions("illegals.check.pv", sender)) {
            SendUnknown.toSender("&cYou do not have permission!", sender);
            return false;
        }
        SendConsole.info("Checking vaults");
        final Map<String, Integer> found = ((Main)this.getBaseCommand().getPlugin()).checkVaults(false);
        SendList.toSender(found, sender, String.valueOf(Messages.getString("prefix")) + Messages.getString("check-header"), Messages.getString("check-line"), 1);
        return true;
    }
}
