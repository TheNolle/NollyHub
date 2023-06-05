package com.thenolle.plugins.nollyhub;

import com.thenolle.plugins.nollyhub.commands.*;
import com.thenolle.plugins.nollyhub.listeners.FoodLevelChangeListener;
import com.thenolle.plugins.nollyhub.listeners.PlayerInteractListener;
import com.thenolle.plugins.nollyhub.listeners.PlayerMoveListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class NollyHub extends JavaPlugin {
    private final SetHubCommand setHubCommand = new SetHubCommand(this);
    private Set<UUID> bypassPlayers;

    @Override
    public void onEnable() {
        createOrReloadConfig();
        validateConfig();

        bypassPlayers = new HashSet<>();

        if (setHubCommand.getLowestPoint() >= this.getConfig().getDouble("hub.y")) {
            this.getConfig().set("hub.y", setHubCommand.getLowestPoint() + 1);
            this.saveConfig();
        }

        Objects.requireNonNull(getCommand("hub")).setExecutor(new HubCommand(this));
        Objects.requireNonNull(getCommand("sethub")).setExecutor(new SetHubCommand(this));
        Objects.requireNonNull(getCommand("hub_feature")).setExecutor(new HubFeatureCommand(this));
        Objects.requireNonNull(getCommand("hub_reload")).setExecutor(new HubReloadCommand(this));
        Objects.requireNonNull(getCommand("hub_bypass")).setExecutor(new HubBypassCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        getLogger().info(MiniMessage.miniMessage().serialize(MiniMessage.miniMessage().deserialize("<green>NollyHub plugin has been enabled.</green>")));
    }

    public Component getMessage(String key) {
        String message = getConfig().getString("messages." + key, "");
        if (message.isEmpty()) {
            return Component.empty();
        }
        return MiniMessage.miniMessage().deserialize(message);
    }

    private void createOrReloadConfig() {
        if (!getDataFolder().exists()) {
            boolean created = getDataFolder().mkdirs();
            if (!created) {
                getLogger().warning("Failed to create plugin data folder.");
                return;
            }
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        } else {
            reloadConfig();
        }
    }

    private void validateConfig() {
        Configuration config = getConfig();
        config.addDefault("permissions.hub.default", "nollyhub.default");
        config.addDefault("permissions.hub.admin", "nollyhub.admin");
        config.addDefault("commands.hub", "hub");

        Objects.requireNonNull(config.getConfigurationSection("permissions")).getKeys(false).stream().filter(key -> !key.equals("hub")).forEach(key -> config.set("permissions." + key, null));
        Objects.requireNonNull(config.getConfigurationSection("commands")).getKeys(false).stream().filter(key -> !key.equals("hub")).forEach(key -> config.set("commands." + key, null));

        saveConfig();
    }

    public boolean toggleBypass(Player player) {
        UUID playerId = player.getUniqueId();
        Set<UUID> bypassPlayers = this.getBypassPlayers();
        if (bypassPlayers.contains(playerId)) {
            bypassPlayers.remove(playerId);
            return false;
        } else {
            bypassPlayers.add(playerId);
            return true;
        }
    }

    public Set<UUID> getBypassPlayers() {
        return bypassPlayers;
    }

    @Override
    public void onDisable() {
        getLogger().info(MiniMessage.miniMessage().serialize(MiniMessage.miniMessage().deserialize("<red>NollyHub plugin has been disabled.</red>")));
    }
}
