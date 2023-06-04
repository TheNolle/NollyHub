package com.thenolle.plugins.nollyhub.commands;

import com.thenolle.plugins.nollyhub.NollyHub;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HubReloadCommand implements CommandExecutor {
    private final NollyHub plugin;
    private final SetHubCommand setHubCommand;

    public HubReloadCommand(NollyHub plugin) {
        this.plugin = plugin;
        this.setHubCommand = new SetHubCommand(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("nollyhub.admin")) {
                double yHub = plugin.getConfig().getDouble("hub.y", 0);
                if (setHubCommand.getLowestPoint() >= yHub) {
                    plugin.getConfig().set("hub.y", setHubCommand.getLowestPoint() + 1);
                    plugin.saveConfig();
                    player.sendMessage(plugin.getMessage("reset_y"));
                }
                plugin.reloadConfig();
                player.sendMessage(plugin.getMessage("reload"));
            } else {
                player.sendMessage(plugin.getMessage("no_permission"));
            }
        }
        return true;
    }
}
