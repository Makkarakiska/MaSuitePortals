package fi.matiaspaavilainen.masuiteportals.bukkit;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PortalManager {

    public static HashMap<World, List<Portal>> portals = new HashMap<>();
    public static List<String> portalNames = new ArrayList<>();

    /**
     * Loads portals from list
     */
    public static void loadPortals() {
        PortalManager.portals.forEach((world, portals) -> portals.forEach(Portal::fillPortal));
    }

    /**
     * Removes portal
     *
     * @param portal specific portal from list
     */
    public static void removePortal(Portal portal) {
        portal.clearPortal();
        portalNames.remove(portal.getName());
        PortalManager.portals.get(portal.getMaxLoc().getWorld()).remove(portal);
    }
}
