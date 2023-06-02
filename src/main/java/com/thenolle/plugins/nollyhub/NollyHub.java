package com.thenolle.plugins.nollyhub;

import com.thenolle.plugins.nollyhub.commands.HubCommand;
import com.thenolle.plugins.nollyhub.commands.HubReloadCommand;
import com.thenolle.plugins.nollyhub.commands.SetHubCommand;
import com.thenolle.plugins.nollyhub.listeners.PlayerMoveListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class NollyHub extends JavaPlugin {
    // Create a new instance of the SetHubCommand class
    private final SetHubCommand setHubCommand = new SetHubCommand(this);

    // Plugin startup logic
    @Override
    public void onEnable() {
        // Create or reload the configuration
        createOrReloadConfig();
        // Perform configuration validation
        validateConfig();
        if (setHubCommand.getLowestPoint() >= this.getConfig().getDouble("hub.y")) {
            this.getConfig().set("hub.y", setHubCommand.getLowestPoint() + 1);
            this.saveConfig();
        }
        // Register the commands
        Objects.requireNonNull(getCommand("hub")).setExecutor(new HubCommand(this));
        Objects.requireNonNull(getCommand("sethub")).setExecutor(new SetHubCommand(this));
        Objects.requireNonNull(getCommand("hub_reload")).setExecutor(new HubReloadCommand(this));
        // Register the listeners
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        // Log that the plugin has been enabled
        getLogger().info(MiniMessage.miniMessage().serialize(MiniMessage.miniMessage().deserialize("<green>NollyHub plugin has been enabled.</green>")));
    }

    // Get a message from the configuration
    public Component getMessage(String key) {
        // Get the message from the configuration
        String message = getConfig().getString("messages." + key, "");
        // Return an empty component if the message is empty
        if (message.isEmpty()) {
            return Component.empty();
        }
        // Return the formatted message
        return MiniMessage.miniMessage().deserialize(message);
    }

    // Create or reload the configuration
    private void createOrReloadConfig() {
        // Create the plugin data folder if it doesn't exist
        if (!getDataFolder().exists()) {
            // Create the plugin data folder
            boolean created = getDataFolder().mkdirs();
            // Log a warning if the plugin data folder could not be created
            if (!created) {
                getLogger().warning("Failed to create plugin data folder.");
                return;
            }
        }
        // Get the configuration file
        File configFile = new File(getDataFolder(), "config.yml");
        // Create the configuration file if it doesn't exist
        if (!configFile.exists()) {
            // Save the default configuration
            saveResource("config.yml", false);
        } else {
            // Reload the configuration
            reloadConfig();
        }
    }

    // Validate the configuration
    private void validateConfig() {
        // Get the configuration
        Configuration config = getConfig();
        // Add the default configuration values
        config.addDefault("permissions.hub.default", "nollyhub.default");
        config.addDefault("permissions.hub.admin", "nollyhub.admin");
        config.addDefault("commands.hub", "hub");
        // Remove the old commands
        Objects.requireNonNull(config.getConfigurationSection("permissions")).getKeys(false).stream()
                .filter(key -> !key.equals("hub"))
                .forEach(key -> config.set("permissions." + key, null));
        // Remove all commands except for the hub command
        Objects.requireNonNull(config.getConfigurationSection("commands")).getKeys(false).stream()
                .filter(key -> !key.equals("hub"))
                .forEach(key -> config.set("commands." + key, null));
        // Save the configuration
        saveConfig();
    }

    // Plugin shutdown logic
    @Override
    public void onDisable() {
        // Log that the plugin has been disabled
        getLogger().info(MiniMessage.miniMessage().serialize(MiniMessage.miniMessage().deserialize("<red>NollyHub plugin has been disabled.</red>")));
    }
}
