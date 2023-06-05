package com.thenolle.plugins.nollyhub.commands;

import com.thenolle.plugins.nollyhub.NollyHub;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HubBypassCommand implements CommandExecutor, TabCompleter {
    private final NollyHub plugin;

    public HubBypassCommand(NollyHub plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            List<String> bypassedPlayers = new ArrayList<>();
            Set<UUID> bypassPlayers = plugin.getBypassPlayers();

            for (UUID playerId : bypassPlayers) {
                Player player = plugin.getServer().getPlayer(playerId);
                if (player != null) {
                    bypassedPlayers.add(player.getName());
                }
            }

            sender.sendMessage(Component.text("Players in bypass mode: " + String.join(", ", bypassedPlayers)));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be executed by a player."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("nollyhub.admin")) {
            player.sendMessage(plugin.getMessage("no_permission"));
            return true;
        }

        boolean bypassEnabled = plugin.toggleBypass(player);

        if (bypassEnabled) {
            player.sendMessage(plugin.getMessage("bypass_enabled"));
        } else {
            player.sendMessage(plugin.getMessage("bypass_disabled"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            List<String> subCommands = new ArrayList<>();
            subCommands.add("list");
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
            return completions;
        }
        return null;
    }
}
