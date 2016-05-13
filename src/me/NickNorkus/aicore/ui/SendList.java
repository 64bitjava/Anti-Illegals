package me.NickNorkus.aicore.ui;

import me.NickNorkus.aicore.lang.*;
import org.bukkit.entity.*;
import java.util.*;

public class SendList
{
    public static void toSender(final Map<?, ?> data, final Object sender, final String headerFormat, final String lineFormat, final int page) {
        final int itemCount = data.size();
        final int pages = itemCount / 9 + 1;
        if (page > pages) {
            SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + Messages.getString("list-page-range").replace("{PAGES}", new StringBuilder(String.valueOf(pages)).toString()), sender);
            return;
        }
        if (page < 1) {
            SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + Messages.getString("list-page-range").replace("{PAGES}", new StringBuilder(String.valueOf(pages)).toString()), sender);
            return;
        }
        final String headerMessage = headerFormat.replace("{PAGES}", new StringBuilder(String.valueOf(pages)).toString()).replace("{PAGE}", new StringBuilder(String.valueOf(page)).toString());
        SendUnknown.toSender(headerMessage, sender);
        if (data.size() == 0) {
            SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + Messages.getString("list-noitems"), sender);
            return;
        }
        for (int i = 0 + (page - 1) * 9; i < 9 * page; ++i) {
            final String lineMessage = lineFormat.replace("{KEY}", new StringBuilder().append(data.keySet().toArray()[i]).toString()).replace("{VALUE}", new StringBuilder().append(data.values().toArray()[i]).toString());
            SendUnknown.toSender(lineMessage, sender);
            if (data.size() <= i + 1) {
                break;
            }
        }
    }

    public static void toPlayer(final Map<String, String> data, final Player player, final String headerFormat, final String lineFormat, final int page) {
        toSender(data, player, headerFormat, lineFormat, page);
    }

    public static void toPlayer(final List<String> list, final Player player) {
        toSender(list, player);
    }

    public static void toSender(final List<String> list, final Object sender) {
        for (final String line : list) {
            SendUnknown.toSender(line, sender);
        }
    }
}
