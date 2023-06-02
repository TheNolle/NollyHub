package com.thenolle.plugins.nollyhub.commands;

import com.thenolle.plugins.nollyhub.NollyHub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HubCommand implements CommandExecutor {
    // Create a new instance of the NollyHub class
    private final NollyHub plugin;
    // Create a new Location object to store the hub location
    private Location hubLocation;

    // Constructor
    public HubCommand(NollyHub plugin) {
        // Initialize the plugin
        this.plugin = plugin;
        // Load the hub location
        loadHubLocation();
    }

    // Run when the command is executed
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // Check if the sender is a player
        if (sender instanceof Player) {
            // Get the player
            Player player = (Player) sender;
            // Check if the player has permission
            if (player.hasPermission("nollyhub.default")) {
                // Load the hub location
                loadHubLocation();
                // Check if the hub location is not set
                if (this.hubLocation == null) {
                    // Send the player a message
                    player.sendMessage(plugin.getMessage("hub_not_set"));
                    return true;
                }
                // Teleport the player to the hub location
                player.teleport(hubLocation);
                // Send the player a message
                player.sendMessage(plugin.getMessage("welcome"));
            } else {
                // If the player does not have permission, send them a message
                player.sendMessage(plugin.getMessage("no_permission"));
            }
        }
        return true;
    }

    // Load the hub location from the config
    public void loadHubLocation() {
        // Check if the hub location is set
        if (plugin.getConfig().isSet("hub.world")) {
            // Get the world name from the config
            String worldName = plugin.getConfig().getString("hub.world");
            // Check if the world name is not null
            assert worldName != null;
            // Get the world from the world name
            World world = Bukkit.getWorld(worldName);
            // Check if the world is not null
            if (world != null) {
                // Get the hub location from the config
                double x = plugin.getConfig().getDouble("hub.x");
                double y = plugin.getConfig().getDouble("hub.y");
                double z = plugin.getConfig().getDouble("hub.z");
                // Set the hub location
                this.hubLocation = new Location(world, x, y, z);
            } else {
                // If the world is not set, set the hub location to null
                this.hubLocation = null;
            }
        } else {
            // If the hub location is not set, set it to null
            this.hubLocation = null;
        }
    }
}
