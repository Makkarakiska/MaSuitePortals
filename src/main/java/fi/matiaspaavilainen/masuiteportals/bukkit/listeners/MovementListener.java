package fi.matiaspaavilainen.masuiteportals.bukkit.listeners;

import fi.matiaspaavilainen.masuiteportals.bukkit.MaSuitePortals;
import fi.matiaspaavilainen.masuiteportals.bukkit.PortalManager;
import fi.matiaspaavilainen.masuiteportals.bukkit.PortalRegion;
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
        Player p = e.getPlayer();

        Block t = e.getTo().getBlock();
        Block f = e.getFrom().getBlock();

        // If the location is the same
        if (f.equals(t)) {
            return;
        }

        // Check if the world is the same
        if (!PortalManager.portals.containsKey(p.getWorld())) {
            return;
        }

        PortalManager.portals.get(p.getWorld()).forEach(portal -> {
            // Get points
            Location corner1 = new Location(p.getWorld(), portal.getMinLoc().getX(), portal.getMinLoc().getY(), portal.getMinLoc().getZ());
            Location corner2 = new Location(p.getWorld(), portal.getMaxLoc().getX(), portal.getMaxLoc().getY(), portal.getMaxLoc().getZ());

            // Create region
            PortalRegion pr = new PortalRegion(corner1, corner2);

            // Check if is region with margin 1
            if (pr.isInWithMarge(p.getLocation(), 1)) {

                // Rotate player
                Vector unitVector = e.getFrom().toVector().subtract(e.getTo().toVector()).normalize();
                Location l = e.getPlayer().getLocation();
                l.setYaw(l.getYaw() + 180);
                unitVector.multiply(2);
                l.add(unitVector);
                p.teleport(l);
                // Send player
                portal.send(p, plugin);
            }
        });

    }
}
