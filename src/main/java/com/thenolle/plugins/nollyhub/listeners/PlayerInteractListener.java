package com.thenolle.plugins.nollyhub.listeners;

import com.thenolle.plugins.nollyhub.NollyHub;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final NollyHub plugin;

    public PlayerInteractListener(NollyHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (plugin.getConfig().getBoolean("features.protected_mode")) {
            if (event.getAction() != Action.PHYSICAL) {
                if (plugin.getBypassPlayers().contains(event.getPlayer().getUniqueId())) {
                    return;
                }
                handleInteraction(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (plugin.getConfig().getBoolean("features.protected_mode")) {
            if (plugin.getBypassPlayers().contains(event.getPlayer().getUniqueId())) {
                return;
            }
            handleInteraction(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (plugin.getConfig().getBoolean("features.protected_mode")) {
            Entity entity = event.getDamager();
            if (!(entity instanceof Player)) {
                return;
            }
            Player player = (Player) entity;
            if (plugin.getBypassPlayers().contains(player.getUniqueId())) {
                return;
            }
            handleInteraction(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (plugin.getConfig().getBoolean("features.protected_mode")) {
            Player player = (Player) event.getEntity();
            if (plugin.getBypassPlayers().contains(player.getUniqueId())) {
                return;
            }
            handleInteraction(player);
            if (!(event.getEntity() instanceof Player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (plugin.getConfig().getBoolean("features.protected_mode")) {
            Player player = event.getPlayer();
            if (plugin.getBypassPlayers().contains(player.getUniqueId())) {
                return;
            }
            handleInteraction(player);
            event.setCancelled(true);
        }
    }

    private void handleInteraction(Player player) {
        player.sendMessage(plugin.getMessage("protected_mode_interact"));
        player.closeInventory();
        player.updateInventory();
    }
}
