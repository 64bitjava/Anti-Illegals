package me.NickNorkus.illegals;

import me.NickNorkus.aicore.*;
import me.NickNorkus.aicore.config.*;
import me.NickNorkus.aicore.schedulers.Timer;
import org.bukkit.plugin.*;
import me.NickNorkus.illegals.commands.*;
import me.NickNorkus.aicore.lang.*;
import org.bukkit.enchantments.*;
import org.bukkit.*;
import com.drtshock.playervaults.vaultmanagement.*;
import org.bukkit.potion.*;
import me.NickNorkus.aicore.utils.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.*;
import me.NickNorkus.aicore.ui.*;

public class Main extends BasePlugin
{
    private TreeMap<String, Integer> enchantmentMaxLevels;
    private TreeMap<String, Integer> effectMaxLevels;
    private TreeMap<String, Integer> effectMaxDuration;
    private List<String> nameBlacklist;
    private List<Material> itemBlacklist;

    public Main() {
        this.enchantmentMaxLevels = new TreeMap<String, Integer>();
        this.effectMaxLevels = new TreeMap<String, Integer>();
        this.effectMaxDuration = new TreeMap<String, Integer>();
        this.nameBlacklist = new ArrayList<String>();
        this.itemBlacklist = new ArrayList<Material>();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.initialize();
    }

    @Override
    public void registerSchedulers() {
        super.registerSchedulers();
        new PlayerChecker((Plugin)this, Configuration.getConfig().getInt("playerchecker.remove-interval"), Configuration.getConfig().getInt("remove-interval"), false);
        new VaultChecker((Plugin)this, Configuration.getConfig().getInt("vaultchecker.remove-interval"), Configuration.getConfig().getInt("remove-interval"), true);
    }

    @Override
    public void registerListeners(final PluginManager pm) {
        super.registerListeners(pm);
    }

    @Override
    public void registerCommands() {
        super.registerCommands();
        new aiCommand((Plugin)this, "antiillegals", "");
    }

    @Override
    public void registerManagers() {
        super.registerManagers();
        SendConsole.info("Loading language '" + Configuration.getConfig().getString("lang") + "'");
        final Messages messages = new Messages((Plugin)this, Configuration.getConfig().getString("lang"));
        MessageLoader.loadMessages(messages);
    }

    @Override
    public void loadConfiguration() {
        Configuration.setConfigVersion(5);
        super.loadConfiguration();
        try {
            final Set<String> enchantmentStrs = (Set<String>)Configuration.getConfig().getConfigurationSection("enchantments").getKeys(false);
            for (final String enchantmentName : enchantmentStrs) {
                try {
                    final Enchantment enchantment = Enchantment.getByName(enchantmentName);
                    final int level = Configuration.getConfig().getInt("enchantments." + enchantmentName + ".max-level");
                    this.enchantmentMaxLevels.put(enchantment.getName(), level);
                }
                catch (Exception ex2) {
                    SendConsole.warning("Unknown enchantment: " + enchantmentName);
                }
            }
            final Set<String> effectStrs = (Set<String>)Configuration.getConfig().getConfigurationSection("effects").getKeys(false);
            for (final String effectName : effectStrs) {
                try {
                    final PotionEffectType effect = PotionEffectType.getByName(effectName);
                    final int level2 = Configuration.getConfig().getInt("effects." + effectName + ".max-level");
                    final int duration = Configuration.getConfig().getInt("effects." + effectName + ".max-duration");
                    this.effectMaxLevels.put(effect.getName(), level2);
                    this.effectMaxDuration.put(effect.getName(), duration);
                }
                catch (Exception ex3) {
                    SendConsole.warning("Unknown effect: " + effectName);
                }
            }
            this.nameBlacklist = (List<String>)Configuration.getConfig().getStringList("name-blacklist");
            final List<String> materialNames = (List<String>)Configuration.getConfig().getStringList("item-blacklist");
            for (final String materialName : materialNames) {
                this.itemBlacklist.add(Material.valueOf(materialName.toUpperCase()));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getEnchantmentMaxLevel(final String enchantment) {
        if (this.enchantmentMaxLevels.containsKey(enchantment)) {
            return this.enchantmentMaxLevels.get(enchantment);
        }
        return -1;
    }

    public int getEffectMaxLevel(final String enchantment) {
        if (this.effectMaxLevels.containsKey(enchantment)) {
            return this.effectMaxLevels.get(enchantment);
        }
        return -1;
    }

    public int getEffectMaxDuration(final String enchantment) {
        if (this.effectMaxDuration.containsKey(enchantment)) {
            return this.effectMaxDuration.get(enchantment);
        }
        return -1;
    }

    public TreeMap<String, Integer> checkVaults(final boolean remove) {
        final TreeMap<String, Integer> output = new TreeMap<String, Integer>();
        final Player[] players = (Player) Bukkit.getOnlinePlayers();
        Player[] array;
        for (int length = (array = players).length, j = 0; j < length; ++j) {
            final Player player = array[j];
            if (!Permissions.hasPermissions("illegals.exempt", player)) {
                List<ItemStack> newItems = new ArrayList<ItemStack>();
                for (int i = 1; i < Configuration.getConfig().getInt("max-vaults"); ++i) {
                    newItems = new ArrayList<ItemStack>();
                    if (UUIDVaultManager.getInstance().vaultExists(player.getUniqueId(), i)) {
                        final Inventory invvault = UUIDVaultManager.getInstance().loadOtherVault(player.getUniqueId(), i, 54);
                        ItemStack[] contents;
                        for (int length2 = (contents = invvault.getContents()).length, k = 0; k < length2; ++k) {
                            final ItemStack item = contents[k];
                            if (item != null) {
                                boolean flag = false;
                                final Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>)item.getEnchantments();
                                for (final Enchantment enchantment : enchantments.keySet()) {
                                    final int level = enchantments.get(enchantment);
                                    if (level > this.getEnchantmentMaxLevel(enchantment.getName())) {
                                        if (output.containsKey(player.getName())) {
                                            output.put(player.getName(), output.get(player.getName()) + 1);
                                        }
                                        else {
                                            output.put(player.getName(), 1);
                                        }
                                        if (remove) {
                                            flag = true;
                                        }
                                        SendConsole.info("Found item with too high level: " + enchantment.getName() + " level=" + level + " player=" + player.getName());
                                        break;
                                    }
                                }
                                for (final Material material : this.itemBlacklist) {
                                    if (item.getType() == material) {
                                        if (output.containsKey(player.getName())) {
                                            output.put(player.getName(), output.get(player.getName()) + 1);
                                        }
                                        else {
                                            output.put(player.getName(), 1);
                                        }
                                        if (!remove) {
                                            continue;
                                        }
                                        flag = true;
                                    }
                                }
                                if (item.getType() == Material.POTION) {
                                    try {
                                        final PotionMeta meta = (PotionMeta)item.getItemMeta();
                                        final Collection<PotionEffect> effects = (Collection<PotionEffect>)meta.getCustomEffects();
                                        if (effects.size() > 1) {
                                            if (output.containsKey(player.getName())) {
                                                output.put(player.getName(), output.get(player.getName()) + 1);
                                            }
                                            else {
                                                output.put(player.getName(), 1);
                                            }
                                            if (remove) {
                                                flag = true;
                                            }
                                        }
                                        for (final PotionEffect effect : effects) {
                                            final int level2 = effect.getAmplifier();
                                            if (level2 > this.getEffectMaxLevel(effect.getType().getName())) {
                                                if (output.containsKey(player.getName())) {
                                                    output.put(player.getName(), output.get(player.getName()) + 1);
                                                }
                                                else {
                                                    output.put(player.getName(), 1);
                                                }
                                                if (remove) {
                                                    flag = true;
                                                }
                                                SendConsole.info("Found item with too high level: " + effect.getType().getName() + " level=" + level2 + " player=" + player.getName());
                                                break;
                                            }
                                            final int duration = effect.getDuration() / 20;
                                            if (duration > this.getEffectMaxDuration(effect.getType().getName())) {
                                                if (output.containsKey(player.getName())) {
                                                    output.put(player.getName(), output.get(player.getName()) + 1);
                                                }
                                                else {
                                                    output.put(player.getName(), 1);
                                                }
                                                if (remove) {
                                                    flag = true;
                                                }
                                                SendConsole.info("Found item with too high level: " + effect.getType().getName() + " level=" + level2 + " player=" + player.getName());
                                                break;
                                            }
                                        }
                                    }
                                    catch (Exception ex) {}
                                }
                                try {
                                    final ItemMeta meta2 = item.getItemMeta();
                                    final String customName = meta2.getDisplayName();
                                    if (customName != null) {
                                        boolean bName = false;
                                        for (final String bannedName : this.nameBlacklist) {
                                            if (ColorUtils.removeColors(customName.replace(" ", "").toLowerCase()).equals(ColorUtils.removeColors(bannedName.replace(" ", "").toLowerCase()))) {
                                                if (remove) {
                                                    flag = true;
                                                }
                                                SendConsole.info("Found item with banned name: " + customName + " player=" + player.getName());
                                                bName = true;
                                                if (output.containsKey(player.getName())) {
                                                    output.put(player.getName(), output.get(player.getName()) + 1);
                                                }
                                                else {
                                                    output.put(player.getName(), 1);
                                                }
                                            }
                                        }
                                        if (!bName && customName.length() > Configuration.getConfig().getInt("max-length")) {
                                            meta2.setDisplayName((String)null);
                                            item.setItemMeta(meta2);
                                            SendConsole.info("Found item with too long name: " + customName + " player=" + player.getName());
                                            if (output.containsKey(player.getName())) {
                                                output.put(player.getName(), output.get(player.getName()) + 1);
                                            }
                                            else {
                                                output.put(player.getName(), 1);
                                            }
                                        }
                                    }
                                }
                                catch (Exception ex2) {}
                                if (!flag) {
                                    newItems.add(item);
                                }
                                else {
                                    newItems.add(null);
                                }
                            }
                            else {
                                newItems.add(null);
                            }
                        }
                        if (remove) {
                            invvault.setContents((ItemStack[])newItems.toArray(new ItemStack[newItems.size()]));
                        }
                    }
                }
            }
        }
        return output;
    }

    public TreeMap<String, Integer> checkPlayers(final boolean remove) {
        final TreeMap<String, Integer> output = new TreeMap<String, Integer>();
        final Player[] players = Bukkit.getOnlinePlayers();
        Player[] array;
        for (int length = (array = players).length, i = 0; i < length; ++i) {
            final Player player = array[i];
            if (!Permissions.hasPermissions("illegals.exempt", player)) {
                try {
                    final Collection<PotionEffect> effects = (Collection<PotionEffect>)player.getActivePotionEffects();
                    for (final PotionEffect effect : effects) {
                        final int level = effect.getAmplifier();
                        if (level > this.getEffectMaxLevel(effect.getType().getName())) {
                            if (output.containsKey(player.getName())) {
                                output.put(player.getName(), output.get(player.getName()) + 1);
                            }
                            else {
                                output.put(player.getName(), 1);
                            }
                            SendConsole.info("Found effect with too high level: " + effect.getType().getName() + " level=" + level + " player=" + player.getName());
                            player.removePotionEffect(effect.getType());
                            break;
                        }
                        final int duration = effect.getDuration() / 20;
                        if (duration > this.getEffectMaxDuration(effect.getType().getName())) {
                            if (output.containsKey(player.getName())) {
                                output.put(player.getName(), output.get(player.getName()) + 1);
                            }
                            else {
                                output.put(player.getName(), 1);
                            }
                            player.removePotionEffect(effect.getType());
                            SendConsole.info("Found effect with too long duration: " + effect.getType().getName() + " level=" + level + " player=" + player.getName());
                            break;
                        }
                    }
                }
                catch (Exception ex) {}
                final PlayerInventory inv = player.getInventory();
                List<ItemStack> newItems = new ArrayList<ItemStack>();
                ItemStack[] contents;
                for (int length2 = (contents = inv.getContents()).length, j = 0; j < length2; ++j) {
                    final ItemStack item = contents[j];
                    if (item != null) {
                        boolean flag = false;
                        final Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>)item.getEnchantments();
                        for (final Enchantment enchantment : enchantments.keySet()) {
                            final int level2 = enchantments.get(enchantment);
                            if (level2 > this.getEnchantmentMaxLevel(enchantment.getName())) {
                                if (output.containsKey(player.getName())) {
                                    output.put(player.getName(), output.get(player.getName()) + 1);
                                }
                                else {
                                    output.put(player.getName(), 1);
                                }
                                if (remove) {
                                    flag = true;
                                }
                                SendConsole.info("Found item with too high level: " + enchantment.getName() + " level=" + level2 + " player=" + player.getName());
                                break;
                            }
                        }
                        for (final Material material : this.itemBlacklist) {
                            if (item.getType() == material) {
                                if (output.containsKey(player.getName())) {
                                    output.put(player.getName(), output.get(player.getName()) + 1);
                                }
                                else {
                                    output.put(player.getName(), 1);
                                }
                                if (!remove) {
                                    continue;
                                }
                                flag = true;
                            }
                        }
                        if (item.getType() == Material.POTION) {
                            try {
                                final PotionMeta meta = (PotionMeta)item.getItemMeta();
                                final Collection<PotionEffect> effects2 = (Collection<PotionEffect>)meta.getCustomEffects();
                                if (effects2.size() > 1) {
                                    if (output.containsKey(player.getName())) {
                                        output.put(player.getName(), output.get(player.getName()) + 1);
                                    }
                                    else {
                                        output.put(player.getName(), 1);
                                    }
                                    if (remove) {
                                        flag = true;
                                    }
                                }
                                for (final PotionEffect effect2 : effects2) {
                                    final int level3 = effect2.getAmplifier();
                                    if (level3 > this.getEffectMaxLevel(effect2.getType().getName())) {
                                        if (output.containsKey(player.getName())) {
                                            output.put(player.getName(), output.get(player.getName()) + 1);
                                        }
                                        else {
                                            output.put(player.getName(), 1);
                                        }
                                        if (remove) {
                                            flag = true;
                                        }
                                        SendConsole.info("Found item with too high level: " + effect2.getType().getName() + " level=" + level3 + " player=" + player.getName());
                                        break;
                                    }
                                    final int duration2 = effect2.getDuration() / 20;
                                    if (duration2 > this.getEffectMaxDuration(effect2.getType().getName())) {
                                        if (output.containsKey(player.getName())) {
                                            output.put(player.getName(), output.get(player.getName()) + 1);
                                        }
                                        else {
                                            output.put(player.getName(), 1);
                                        }
                                        if (remove) {
                                            flag = true;
                                        }
                                        SendConsole.info("Found item with too high level: " + effect2.getType().getName() + " level=" + level3 + " player=" + player.getName());
                                        break;
                                    }
                                }
                            }
                            catch (Exception ex2) {}
                        }
                        try {
                            final ItemMeta meta2 = item.getItemMeta();
                            final String customName = meta2.getDisplayName();
                            if (customName != null) {
                                boolean bName = false;
                                for (final String bannedName : this.nameBlacklist) {
                                    if (ColorUtils.removeColors(customName.replace(" ", "").toLowerCase()).equals(ColorUtils.removeColors(bannedName.replace(" ", "").toLowerCase()))) {
                                        if (remove) {
                                            flag = true;
                                        }
                                        SendConsole.info("Found item with banned name: " + customName + " player=" + player.getName());
                                        bName = true;
                                        if (output.containsKey(player.getName())) {
                                            output.put(player.getName(), output.get(player.getName()) + 1);
                                        }
                                        else {
                                            output.put(player.getName(), 1);
                                        }
                                    }
                                }
                                if (!bName && customName.length() > Configuration.getConfig().getInt("max-length")) {
                                    meta2.setDisplayName((String)null);
                                    item.setItemMeta(meta2);
                                    SendConsole.info("Found item with too long name: " + customName + " player=" + player.getName());
                                    if (output.containsKey(player.getName())) {
                                        output.put(player.getName(), output.get(player.getName()) + 1);
                                    }
                                    else {
                                        output.put(player.getName(), 1);
                                    }
                                }
                            }
                        }
                        catch (Exception ex3) {}
                        if (!flag) {
                            newItems.add(item);
                        }
                        else {
                            newItems.add(null);
                        }
                    }
                    else {
                        newItems.add(null);
                    }
                }
                if (remove) {
                    inv.setContents((ItemStack[])newItems.toArray(new ItemStack[newItems.size()]));
                }
                newItems = new ArrayList<ItemStack>();
                ItemStack[] armorContents;
                for (int length3 = (armorContents = inv.getArmorContents()).length, k = 0; k < length3; ++k) {
                    final ItemStack item = armorContents[k];
                    if (item != null) {
                        boolean flag = false;
                        final Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>)item.getEnchantments();
                        for (final Enchantment enchantment : enchantments.keySet()) {
                            final int level2 = enchantments.get(enchantment);
                            if (level2 > this.getEnchantmentMaxLevel(enchantment.getName())) {
                                if (output.containsKey(player.getName())) {
                                    output.put(player.getName(), output.get(player.getName()) + 1);
                                }
                                else {
                                    output.put(player.getName(), 1);
                                }
                                if (remove) {
                                    flag = true;
                                }
                                SendConsole.info("Found item with too high level: " + enchantment.getName() + " level=" + level2);
                                break;
                            }
                        }
                        if (!flag) {
                            newItems.add(item);
                        }
                        else {
                            newItems.add(null);
                        }
                    }
                    else {
                        newItems.add(null);
                    }
                }
                if (remove) {
                    inv.setArmorContents((ItemStack[])newItems.toArray(new ItemStack[newItems.size()]));
                }
            }
        }
        return output;
    }

    class PlayerChecker extends Timer
    {
        public PlayerChecker(final Plugin plugin, final int interval, final int wait, final boolean async) {
            super(plugin, interval, wait, async);
            SendConsole.info("Starting player check timer with an interval of " + interval + " seconds");
        }

        @Override
        public void run() {
            super.run();
            final TreeMap<String, Integer> map = NoBannedItems.this.checkPlayers(true);
            for (final String item : map.keySet()) {
                Player[] onlinePlayers;
                for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
                    final Player player = onlinePlayers[i];
                    if (Permissions.hasPermissions("illegals.check", player)) {
                        SendGame.toPlayer(Messages.getString("report").replace("{PLAYER}", item), player);
                    }
                }
            }
        }
    }

    class VaultChecker extends Timer
    {
        public VaultChecker(final Plugin plugin, final int interval, final int wait, final boolean async) {
            super(plugin, interval, wait, async);
            SendConsole.info("Starting vault check timer with an interval of " + interval + " seconds");
        }

        @Override
        public void run() {
            super.run();
            final TreeMap<String, Integer> map = NoBannedItems.this.checkVaults(true);
            for (final String item : map.keySet()) {
                Player[] onlinePlayers;
                for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
                    final Player player = onlinePlayers[i];
                    if (Permissions.hasPermissions("illegals.check", player)) {
                        SendGame.toPlayer(Messages.getString("report").replace("{PLAYER}", item), player);
                    }
                }
            }
        }
    }
}
