package com.thenolle.plugins.nollyhub.listeners;

import com.thenolle.plugins.nollyhub.NollyHub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
    private final NollyHub plugin;

    public FoodLevelChangeListener(NollyHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (plugin.getConfig().getBoolean("features.disable_hunger")) {
            disableHunger(event);
        }
    }

    public void disableHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!plugin.getBypassPlayers().contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
