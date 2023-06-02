package com.thenolle.plugins.nollyhub.listeners;

import com.thenolle.plugins.nollyhub.NollyHub;
import com.thenolle.plugins.nollyhub.commands.SetHubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    // Create a new instance of the NollyHub class
    private final NollyHub plugin;
    // Create a new instance of the SetHubCommand class
    private final SetHubCommand setHubCommand;

    // Constructor
    public PlayerMoveListener(NollyHub plugin) {
        // Set the plugin
        this.plugin = plugin;
        // Set the setHubCommand
        this.setHubCommand = new SetHubCommand(plugin);
    }

    // Player move event
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Get the player
        Player player = event.getPlayer();
        // Get the player location
        Location playerLocation = event.getPlayer().getLocation();
        // Check if the player is below the lowest point
        if (playerLocation.getY() < setHubCommand.getLowestPoint()) {
            // Get the hub world name from the config
            String worldName = plugin.getConfig().getString("hub.world");
            // Check if the hub world is set
            if (worldName != null) {
                // Create a new location using the hub location from the config
                Location hubLocation = new Location(Bukkit.getWorld(worldName), plugin.getConfig().getDouble("hub.x"), plugin.getConfig().getDouble("hub.y"), plugin.getConfig().getDouble("hub.z"));
                // Set the yaw
                hubLocation.setYaw((float) plugin.getConfig().getDouble("hub.yaw"));
                // Teleport to the hub
                player.teleport(hubLocation);
                // Send the player a message
                player.sendMessage(plugin.getMessage("welcome"));
            } else {
                // Teleport to the world spawn
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                // Send the player a message
                player.sendMessage(plugin.getMessage("hub_not_set"));
            }
        }
    }
}
