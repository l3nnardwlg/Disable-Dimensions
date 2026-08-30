package com.agfstudios.disabledimensions;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class DimensionAccessListener implements Listener {
    private final DisableDimensionsPlugin plugin;
    private final DimensionAccessService accessService;

    public DimensionAccessListener(DisableDimensionsPlugin plugin, DimensionAccessService accessService) {
        this.plugin = plugin;
        this.accessService = accessService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPortal(PlayerPortalEvent event) {
        if (!accessService.blockPortals() || event.getTo() == null || event.getTo().getWorld() == null) {
            return;
        }

        Player player = event.getPlayer();
        World.Environment target = event.getTo().getWorld().getEnvironment();
        if (!accessService.isBlocked(player, target)) {
            return;
        }

        event.setCancelled(true);
        accessService.notifyBlocked(player, target);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!accessService.blockPlayerTeleports() || event instanceof PlayerPortalEvent
                || event.getTo() == null || event.getTo().getWorld() == null) {
            return;
        }

        Player player = event.getPlayer();
        World.Environment target = event.getTo().getWorld().getEnvironment();
        if (!accessService.isBlocked(player, target)) {
            return;
        }

        event.setCancelled(true);
        accessService.notifyBlocked(player, target);
        plugin.getLogger().fine(() -> "Blocked teleport of " + player.getName() + " to " + target);
    }
}
