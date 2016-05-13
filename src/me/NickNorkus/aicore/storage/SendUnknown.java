package me.NickNorkus.aicore.storage;

import org.bukkit.entity.*;

public class SendUnknown
{
    public static void toSender(final String message, final Object sender) {
        if (sender instanceof Player) {
            SendGame.toPlayer(message, (Player)sender);
        }
        else {
            SendConsole.message(message);
        }
    }
}
