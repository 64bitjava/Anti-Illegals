package me.NickNorkus.illegals.commands;

import me.NickNorkus.aicore.commands.*;
import org.bukkit.*;
import me.NickNorkus.aicore.lang.*;
import me.NickNorkus.aicore.storage.*;
import org.bukkit.potion.*;
import me.NickNorkus.aicore.utils.*;
import org.bukkit.entity.*;
import java.util.*;

public class EffectsCommand extends SubCommand
{
    public EffectsCommand(final String command, final String permission, final String description) {
        super(command, permission, description);
    }

    @Override
    public boolean onCommand(final Object sender, final String[] args) {
        if (!super.onCommand(sender, args)) {
            return false;
        }
        if (args.length == 2) {
            final String playerName = args[1];
            final Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + Messages.getString("effects-noplayer"), sender);
                return false;
            }
            final Collection<PotionEffect> effects = (Collection<PotionEffect>)player.getActivePotionEffects();
            SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + Messages.getString("effects-header").replace("{PLAYER}", player.getName()), sender);
            for (final PotionEffect effect : effects) {
                String effectName = effect.getType().getName();
                effectName = effectName.toLowerCase().replace("_", " ");
                effectName = String.valueOf(effectName.substring(0, 1).toUpperCase()) + effectName.substring(1);
                SendUnknown.toSender(Messages.getString("effects-line").replace("{EFFECT}", effectName).replace("{DURATION}", new StringBuilder(String.valueOf(DateUtils.toString(effect.getDuration() / 20L, "mm:ss"))).toString()).replace("{LEVEL}", new StringBuilder().append(effect.getAmplifier() + 1).toString()), sender);
            }
        }
        else {
            SendUnknown.toSender(String.valueOf(Messages.getString("prefix")) + "&c/nbi effects &4<player>", sender);
        }
        return true;
    }
}
