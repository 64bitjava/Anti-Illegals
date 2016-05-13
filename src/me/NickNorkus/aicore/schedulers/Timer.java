package me.NickNorkus.aicore.schedulers;

import org.bukkit.plugin.*;

public class Timer implements Runnable
{
    private int count;
    private int interval;
    private Plugin plugin;

    public Timer(final Plugin plugin, final int interval, final int wait, final boolean async) {
        this.count = 0;
        this.interval = 0;
        this.plugin = null;
        this.plugin = plugin;
        this.interval = interval;
        if (async) {
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, (Runnable)this, (long)(wait * 20), (long)(interval * 20));
        }
        else {
            plugin.getServer().getScheduler().runTaskTimer(plugin, (Runnable)this, (long)(wait * 20), (long)(interval * 20));
        }
    }

    public int getInterval() {
        return this.interval;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public void run() {
        ++this.count;
    }
}
