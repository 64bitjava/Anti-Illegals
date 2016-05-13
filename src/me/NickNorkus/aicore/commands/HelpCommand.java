package me.NickNorkus.aicore.commands;

import me.NickNorkus.aicore.ui.*;
import java.util.*;

public class HelpCommand extends SubCommand
{
    private List<String> help;

    public HelpCommand(final String command, final String permission, final String description, final List<String> help) {
        super(command, permission, description);
        this.help = new ArrayList<String>();
        this.help = help;
    }

    public HelpCommand(final String command, final String permission, final String description) {
        super(command, permission, description);
        this.help = new ArrayList<String>();
    }

    public List<String> getHelpMessages() {
        return this.help;
    }

    public void setHelpMessages(final List<String> help) {
        this.help = help;
    }

    @Override
    public boolean onCommand(final Object sender, final String[] args) {
        if (!super.onCommand(sender, args)) {
            return false;
        }
        for (final String line : this.getHelpMessages()) {
            SendUnknown.toSender(line, sender);
        }
        return true;
    }
}
