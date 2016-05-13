package me.NickNorkus.aicore.utils;

import org.bukkit.entity.*;

public class Permissions
{
    public static boolean allowOpPermissions;
    public static boolean denyOpPermissions;

    static {
        Permissions.allowOpPermissions = true;
        Permissions.denyOpPermissions = false;
    }

    public static boolean hasPermissions(final String permission, final Object sender) {
        Player player = null;
        if (!(sender instanceof Player)) {
            return true;
        }
        player = (Player)sender;
        return (player.isOp() && Permissions.allowOpPermissions) || ((!player.isOp() || !Permissions.denyOpPermissions) && player.hasPermission(permission));
    }
}
