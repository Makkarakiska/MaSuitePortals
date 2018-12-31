/*
 * Original author: AltFreq
 * Updated version for MaSuite: Masa
 */

package fi.matiaspaavilainen.masuiteportals.bukkit.listeners;

import fi.matiaspaavilainen.masuiteportals.bukkit.Portal;
import fi.matiaspaavilainen.masuiteportals.bukkit.PortalManager;
import fi.matiaspaavilainen.masuiteportals.bukkit.PortalRegion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class PhysicsListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent e) {
        if (!PortalManager.portals.containsKey(e.getBlock().getWorld())) {
            return;
        }

        for (Portal portal : PortalManager.portals.get(e.getBlock().getWorld())) {
            PortalRegion pr = new PortalRegion(portal.getMinLoc(), portal.getMaxLoc());
            if (pr.isIn(e.getBlock().getLocation())) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPhysics(BlockFromToEvent e) {
        if (!PortalManager.portals.containsKey(e.getBlock().getWorld())) {
            return;
        }

        for (Portal portal : PortalManager.portals.get(e.getBlock().getWorld())) {
            PortalRegion pr = new PortalRegion(portal.getMinLoc(), portal.getMaxLoc());
            if (pr.isIn(e.getBlock().getLocation())) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!PortalManager.portals.containsKey(e.getBlock().getWorld())) {
            return;
        }

        for (Portal portal : PortalManager.portals.get(e.getBlock().getWorld())) {
            PortalRegion pr = new PortalRegion(portal.getMinLoc(), portal.getMaxLoc());
            if (pr.isIn(e.getBlock().getLocation())) {
                e.setCancelled(true);
            }
        }

    }
}
