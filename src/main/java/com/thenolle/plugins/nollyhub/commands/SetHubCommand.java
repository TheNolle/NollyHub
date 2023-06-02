package com.thenolle.plugins.nollyhub.commands;

import com.thenolle.plugins.nollyhub.NollyHub;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHubCommand implements CommandExecutor {
    // Create a new instance of the NollyHub class
    private final NollyHub plugin;

    // Constructor
    public SetHubCommand(NollyHub plugin) {
        // Set the plugin
        this.plugin = plugin;
    }

    // Run when the command is executed
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // Check if the sender is a player
        if (sender instanceof Player) {
            // Get the player
            Player player = (Player) sender;
            // Check if the player has permission
            if (player.hasPermission("nollyhub.admin")) {
                // Check if the player is below or at the lowest point
                if (player.getLocation().getY() <= this.getLowestPoint()) {
                    // Send the player a message
                    player.sendMessage(plugin.getMessage("too_low"));
                    return true;
                }
                // Get the player's location
                Location hubLocation = player.getLocation();
                // Set the hub location in the config
                plugin.getConfig().set("hub.world", hubLocation.getWorld().getName());
                plugin.getConfig().set("hub.x", hubLocation.getX());
                plugin.getConfig().set("hub.y", hubLocation.getY());
                plugin.getConfig().set("hub.z", hubLocation.getZ());
                plugin.getConfig().set("hub.yaw", getYawForDirection(hubLocation.getYaw()));
                // Save the config
                plugin.saveConfig();
                // Send the player a message
                player.sendMessage(plugin.getMessage("set_hub"));
            } else {
                // If the player doesn't have permission, send them a message
                player.sendMessage(plugin.getMessage("no_permission"));
            }
        }
        return true;
    }

    // Get the yaw for the direction the player is facing
    private float getYawForDirection(float yaw) {
        // If the yaw is negative, add 360 to make it positive
        if (yaw < 0) {
            yaw += 360;
        }
        // Round the yaw to the nearest 90 degrees
        return Math.round(yaw / 90) * 90;
    }

    // Get the lowest point in the world
    public int getLowestPoint() {
        // Set the lowest block to the highest possible value
        int lowestY = Integer.MAX_VALUE;
        // Loop through all the worlds
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            // Loop through all the loaded chunks in the world
            for (Chunk chunk : world.getLoadedChunks()) {
                // Loop through all the blocks in the chunk
                for (int x = 0; x < 16; x++) {
                    // Loop through all the blocks in the chunk
                    for (int z = 0; z < 16; z++) {
                        // Get the highest block at the current x and z
                        int y = world.getHighestBlockYAt(chunk.getX() * 16 + x, chunk.getZ() * 16 + z);
                        // If the block is lower than the current lowest block, set the lowest block to the current block
                        if (y < lowestY) {
                            lowestY = y;
                        }
                    }
                }
            }
        }
        // If no blocks were found, return 0
        return lowestY != Integer.MAX_VALUE ? lowestY : 0;
    }
}
