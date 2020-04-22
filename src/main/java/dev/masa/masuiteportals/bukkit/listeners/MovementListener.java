package dev.masa.masuiteportals.bukkit.listeners;

import dev.masa.masuitecore.core.adapters.BukkitAdapter;
import dev.masa.masuiteportals.bukkit.MaSuitePortals;
import dev.masa.masuiteportals.bukkit.PortalRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class MovementListener implements Listener {

    private MaSuitePortals plugin;

    private HashMap<UUID, Long> inPortal = new HashMap<>();

    public MovementListener(MaSuitePortals p) {
        plugin = p;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Block t = e.getTo().getBlock();
        Block f = e.getFrom().getBlock();

        // If the location is the same
        if (f.equals(t)) {
            return;
        }

        // Check if the world is the same
        if (!plugin.portalManager.getWorlds().contains(player.getWorld())) {
            return;
        }


        plugin.portalManager.getPortalsFromWorld(player.getWorld()).forEach(portal -> {
            // Create region
            Location minLoc = BukkitAdapter.adapt(portal.getMinLocation());
            Location maxLoc = BukkitAdapter.adapt(portal.getMaxLocation());
            PortalRegion pr = new PortalRegion(minLoc, maxLoc);

            // Check if is region with margin 1
            if (pr.isInWithMarge(player.getLocation(), 1)) {

                // Rotate player
                Vector unitVector = e.getFrom().toVector().subtract(e.getTo().toVector()).normalize();
                Location l = e.getPlayer().getLocation();
                l.setYaw(l.getYaw() + 180);
                unitVector.multiply(2);
                l.add(unitVector);
                player.teleport(l);
                // Send player
                plugin.portalManager.sendPlayer(player, portal);
            }
        });

    }
}
