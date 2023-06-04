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
    private final NollyHub plugin;

    public SetHubCommand(NollyHub plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("nollyhub.admin")) {
                if (player.getLocation().getY() <= this.getLowestPoint()) {
                    player.sendMessage(plugin.getMessage("too_low"));
                    return true;
                }

                Location hubLocation = player.getLocation();
                plugin.getConfig().set("hub.world", hubLocation.getWorld().getName());
                plugin.getConfig().set("hub.x", hubLocation.getX());
                plugin.getConfig().set("hub.y", hubLocation.getY());
                plugin.getConfig().set("hub.z", hubLocation.getZ());
                plugin.getConfig().set("hub.yaw", getYawForDirection(hubLocation.getYaw()));

                plugin.saveConfig();

                player.sendMessage(plugin.getMessage("set_hub"));
            } else {
                player.sendMessage(plugin.getMessage("no_permission"));
            }
        }
        return true;
    }

    private float getYawForDirection(float yaw) {
        if (yaw < 0) {
            yaw += 360;
        }
        return Math.round(yaw / 90) * 90;
    }

    public int getLowestPoint() {
        int lowestY = Integer.MAX_VALUE;

        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        int y = world.getHighestBlockYAt(chunk.getX() * 16 + x, chunk.getZ() * 16 + z);
                        if (y < lowestY) {
                            lowestY = y;
                        }
                    }
                }
            }
        }
        return lowestY != Integer.MAX_VALUE ? lowestY : 0;
    }
}
