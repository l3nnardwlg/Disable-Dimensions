package com.agfstudios.disabledimensions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class DisableDimensionsPlugin extends JavaPlugin {
    private DimensionAccessService accessService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.accessService = new DimensionAccessService(this);
        getServer().getPluginManager().registerEvents(new DimensionAccessListener(this, accessService), this);
        getLogger().info("Disable-Dimensions enabled.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("disabledimensions")) {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("disabledimensions.admin")) {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }

            reloadConfig();
            accessService.reload();
            sender.sendMessage("Disable-Dimensions configuration reloaded.");
            return true;
        }

        sender.sendMessage("Usage: /" + label + " reload");
        return true;
    }
}
