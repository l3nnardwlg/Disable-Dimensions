package com.agfstudios.disabledimensions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class DimensionAccessService {
    private final DisableDimensionsPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private boolean netherEnabled;
    private boolean endEnabled;
    private boolean notifyPlayer;
    private boolean blockPlayerTeleports;
    private boolean blockPortals;
    private String bypassPermission;
    private Component netherMessage;
    private Component endMessage;

    public DimensionAccessService(DisableDimensionsPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        FileConfiguration config = plugin.getConfig();
        this.netherEnabled = config.getBoolean("dimensions.nether.enabled", false);
        this.endEnabled = config.getBoolean("dimensions.end.enabled", false);
        this.notifyPlayer = config.getBoolean("behavior.notify-player", true);
        this.blockPlayerTeleports = config.getBoolean("behavior.block-player-teleports", true);
        this.blockPortals = config.getBoolean("behavior.block-portals", true);
        this.bypassPermission = config.getString("bypass-permission", "disabledimensions.bypass");
        this.netherMessage = miniMessage.deserialize(config.getString("dimensions.nether.message", "<red>The Nether is currently disabled.</red>"));
        this.endMessage = miniMessage.deserialize(config.getString("dimensions.end.message", "<red>The End is currently disabled.</red>"));
    }

    public boolean isBlocked(Player player, World.Environment environment) {
        if (player.hasPermission(bypassPermission)) {
            return false;
        }
        return switch (environment) {
            case NETHER -> !netherEnabled;
            case THE_END -> !endEnabled;
            default -> false;
        };
    }

    public void notifyBlocked(Player player, World.Environment environment) {
        if (!notifyPlayer) {
            return;
        }
        switch (environment) {
            case NETHER -> player.sendMessage(netherMessage);
            case THE_END -> player.sendMessage(endMessage);
            default -> { }
        }
    }

    public boolean blockPlayerTeleports() {
        return blockPlayerTeleports;
    }

    public boolean blockPortals() {
        return blockPortals;
    }
}
