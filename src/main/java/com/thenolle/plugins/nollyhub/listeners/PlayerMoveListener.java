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
    private final NollyHub plugin;
    private final SetHubCommand setHubCommand;

    public PlayerMoveListener(NollyHub plugin) {
        this.plugin = plugin;
        this.setHubCommand = new SetHubCommand(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = event.getPlayer().getLocation();

        if (playerLocation.getY() < setHubCommand.getLowestPoint()) {
            String worldName = plugin.getConfig().getString("hub.world");
            if (worldName != null) {
                Location hubLocation = new Location(Bukkit.getWorld(worldName), plugin.getConfig().getDouble("hub.x"), plugin.getConfig().getDouble("hub.y"), plugin.getConfig().getDouble("hub.z"));
                hubLocation.setYaw((float) plugin.getConfig().getDouble("hub.yaw"));

                player.teleport(hubLocation);
                player.sendMessage(plugin.getMessage("welcome"));
            } else {
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                player.sendMessage(plugin.getMessage("hub_not_set"));
            }
        }
    }
}
