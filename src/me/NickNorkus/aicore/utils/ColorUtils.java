package me.NickNorkus.aicore.utils;

import org.bukkit.*;

public class ColorUtils
{
    public static String toColors(final String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("(&([a-fk-or0-9]))", "§$2");
    }

    public static String removeColors(final String input) {
        if (input == null) {
            return null;
        }
        return ChatColor.stripColor(input.replaceAll("(&([a-fk-or0-9]))", ""));
    }
}
