package dev.masa.masuiteportals.bukkit.listeners;

import dev.masa.masuitecore.core.adapters.BukkitAdapter;
import dev.masa.masuiteportals.bukkit.MaSuitePortals;
import dev.masa.masuiteportals.bukkit.PortalRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class PhysicsListener implements Listener {

    private MaSuitePortals plugin;

    public PhysicsListener(MaSuitePortals plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        this.checkIfInPortal(event.getBlock(), event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPhysics(BlockFromToEvent event) {
        this.checkIfInPortal(event.getBlock(), event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        this.checkIfInPortal(event.getBlock(), event);
    }


    private void checkIfInPortal(Block block, Cancellable event) {
        if (!plugin.portalManager.getWorlds().contains(block.getWorld())) {
            return;
        }

        plugin.portalManager.getPortalsFromWorld(block.getWorld()).forEach(portal -> {
            Location minLoc = BukkitAdapter.adapt(portal.getMinLocation());
            Location maxLoc = BukkitAdapter.adapt(portal.getMaxLocation());
            PortalRegion pr = new PortalRegion(minLoc, maxLoc);
            if (pr.isIn(block.getLocation())) {
                event.setCancelled(true);
            }
        });
    }
}
