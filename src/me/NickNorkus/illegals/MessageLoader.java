package me.NickNorkus.illegals;

import me.NickNorkus.aicore.lang.*;
import java.util.*;

public class MessageLoader
{
    public static void loadMessages(final Messages messages) {
        messages.set("prefix", "&f[&9Anti-Illegals&f] ");
        messages.set("plugin-reloaded", "&aConfiguration reloaded!");
        messages.set("no-permission", "&cYou do not have permissions!");
        messages.set("invalid-arguments", "&cInvalid arguments!");
        messages.set("list-page-range", "&cThe page can be from 1-{PAGES}!");
        messages.set("list-noitems", "&cNo items found!");
        messages.set("check-header", "&7Players with illegal items:");
        messages.set("check-line", "&8{KEY} &7has {VALUE} illegal items");
        messages.set("effects-header", "&7Potion effects of {PLAYER}:");
        messages.set("effects-line", "&6{EFFECT} {LEVEL} &8&o({DURATION})");
        messages.set("effects-noplayer", "&cSpecified player not found!");
        messages.set("removed-header", "&7Removed illegal items:");
        messages.set("removed-line", "&8{KEY} &7had {VALUE} illegal items");
        final List<String> help = new ArrayList<String>();
        help.add("&8/&aai check &f- Check for illegal items");
        help.add("&8/&aai remove &f- Remove illegal items");
        help.add("&8/&aai reload &f- Reload the plugin");
        help.add("&8/&aai effects &2<player> &f- Show potion effects");
        messages.set("report", "&7{PLAYER}&8 had illegal items!");
        messages.set("help", help);
    }
}
