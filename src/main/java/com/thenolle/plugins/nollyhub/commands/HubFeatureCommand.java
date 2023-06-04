package com.thenolle.plugins.nollyhub.commands;

import com.thenolle.plugins.nollyhub.NollyHub;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HubFeatureCommand implements CommandExecutor, TabCompleter {
    private final NollyHub plugin;

    public HubFeatureCommand(NollyHub plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessage("features.how_to_use"));
            return true;
        }

        String feature = args[0].toLowerCase();
        String action = args[1].toLowerCase();

        if (!isValidFeature(feature)) {
            sender.sendMessage(plugin.getMessage("features.invalid").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
            return true;
        }

        boolean enabled = plugin.getConfig().getBoolean("features." + feature);

        switch (action) {
            case "enable":
                if (enabled) {
                    sender.sendMessage(plugin.getMessage("features.already_enabled").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
                    break;
                }
                plugin.getConfig().set("features." + feature, true);
                plugin.saveConfig();
                sender.sendMessage(plugin.getMessage("features.enabled").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
                break;
            case "disable":
                if (!enabled) {
                    sender.sendMessage(plugin.getMessage("features.already_disabled").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
                    break;
                }
                plugin.getConfig().set("features." + feature, false);
                plugin.saveConfig();
                sender.sendMessage(plugin.getMessage("features.disabled").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
                break;
            case "status":
                if (enabled) {
                    sender.sendMessage(plugin.getMessage("features.enabled_status").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
                    break;
                }
                sender.sendMessage(plugin.getMessage("features.disabled_status").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
                break;
            default:
                sender.sendMessage(plugin.getMessage("invalid_action").replaceText(builder -> builder.matchLiteral("{feature}").replacement(feature)));
                break;
        }
        return true;
    }

    private boolean isValidFeature(String feature) {
        List<String> featureNames = new ArrayList<>(Objects.requireNonNull(plugin.getConfig().getConfigurationSection("features")).getKeys(false));
        return featureNames.contains(feature);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            List<String> featureNames = new ArrayList<>(Objects.requireNonNull(plugin.getConfig().getConfigurationSection("features")).getKeys(false));
            StringUtil.copyPartialMatches(args[0], featureNames, completions);
            return completions;
        } else if (args.length == 2) {
            List<String> completions = Arrays.asList("enable", "disable", "status");
            return StringUtil.copyPartialMatches(args[1], completions, new ArrayList<>());
        }
        return null;
    }
}
