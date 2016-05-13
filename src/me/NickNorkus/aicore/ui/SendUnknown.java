package me.NickNorkus.aicore.ui;

import org.bukkit.entity.*;

public class SendUnknown
{
    public static void toSender(final String message, final Object sender) {
        if (sender instanceof Player) {
            SendGame.toPlayer(message, (Player)sender);
        }
        else {
            SendConsole.info(message);
        }
    }
}
