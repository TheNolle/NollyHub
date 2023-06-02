package com.thenolle.plugins.nollyhub.commands;

import com.thenolle.plugins.nollyhub.NollyHub;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HubReloadCommand implements CommandExecutor {
    // Create a new instance of the NollyHub class
    private final NollyHub plugin;
    // Create a new instance of the SetHubCommand class
    private final SetHubCommand setHubCommand;

    // Constructor
    public HubReloadCommand(NollyHub plugin) {
        // Initialize the plugin
        this.plugin = plugin;
        // Initialize the SetHubCommand class
        this.setHubCommand = new SetHubCommand(plugin);
    }

    // Run when the command is executed
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // Check if sender is player
        if (sender instanceof Player) {
            // Get player
            Player player = (Player) sender;
            // Check if player has permission
            if (player.hasPermission("nollyhub.admin")) {
                // Get hub y
                double yHub = plugin.getConfig().getDouble("hub.y", 0);
                // Check if the lowest point is higher than the hub
                if (setHubCommand.getLowestPoint() >= yHub) {
                    // Set config
                    plugin.getConfig().set("hub.y", setHubCommand.getLowestPoint() + 1);
                    // Save config
                    plugin.saveConfig();
                    // Send message to player
                    player.sendMessage(plugin.getMessage("reset_y"));
                    return true;
                }
                // Reload config
                plugin.reloadConfig();
                // Send message to player
                player.sendMessage(plugin.getMessage("reload"));
            } else {
                // Send message to player
                player.sendMessage(plugin.getMessage("no_permission"));
            }
        }
        return true;
    }
}
