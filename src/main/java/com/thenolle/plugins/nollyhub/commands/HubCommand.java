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
    private final NollyHub plugin;
    private Location hubLocation;

    public HubCommand(NollyHub plugin) {
        this.plugin = plugin;
        loadHubLocation();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("nollyhub.default")) {
                loadHubLocation();

                if (this.hubLocation == null) {
                    player.sendMessage(plugin.getMessage("hub_not_set"));
                    return true;
                }

                player.teleport(hubLocation);
                player.sendMessage(plugin.getMessage("welcome"));
            } else {
                player.sendMessage(plugin.getMessage("no_permission"));
            }
        }
        return true;
    }

    public void loadHubLocation() {
        if (plugin.getConfig().isSet("hub.world")) {
            String worldName = plugin.getConfig().getString("hub.world");

            assert worldName != null;
            World world = Bukkit.getWorld(worldName);

            if (world != null) {
                double x = plugin.getConfig().getDouble("hub.x");
                double y = plugin.getConfig().getDouble("hub.y");
                double z = plugin.getConfig().getDouble("hub.z");

                this.hubLocation = new Location(world, x, y, z);
            } else {
                this.hubLocation = null;
            }
        } else {
            this.hubLocation = null;
        }
    }
}
