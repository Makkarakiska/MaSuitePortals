package fi.matiaspaavilainen.masuiteportals.bukkit;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Portal {

    private String name;
    private String type;
    private String destination;
    private Location minLoc, maxLoc;
    private String fillType;

    /**
     * An empty constructor for Portal
     */
    public Portal() {
    }

    /**
     * Constructor for Portal
     *
     * @param name        portal's name
     * @param type        portal's type (warp/server)
     * @param destination portal's destination (server or warp name)
     * @param fillType    what {@link Material} will be used to fill the portal
     * @param minLoc      the first corner of portal
     * @param maxLoc      the second corner of portal
     */
    public Portal(String name, String type, String destination, String fillType, Location minLoc, Location maxLoc) {
        this.name = name;
        this.type = type;
        this.destination = destination;
        this.fillType = fillType;
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
    }

    /**
     * Sends player trough portal
     *
     * @param player to send
     * @param plugin to use in plugin messages
     */
    public void send(Player player, MaSuitePortals plugin) {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            if (getType().equals("server")) {
                out.writeUTF("ConnectOther");
                out.writeUTF(player.getName());
                out.writeUTF(getDestination());
                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } else if (getType().equals("warp")) {

                out.writeUTF("WarpPlayerCommand");
                out.writeUTF(player.getName());
                out.writeUTF("console");
                out.writeUTF(getDestination());

                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Fills portal
     */
    public void fillPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> {
            if (block.getBlockData().getMaterial().equals(Material.AIR)) {
                // Portal manipulation if type is nether_portal
                if (getFillType().toLowerCase().contains("nether_portal")) {
                    block.setType(Material.NETHER_PORTAL);
                    Orientable orientable = (Orientable) block.getBlockData();
                    switch (getFillType().toLowerCase()) {
                        case ("nether_portal_north"):
                            orientable.setAxis(Axis.X);
                            break;
                        case ("nether_portal_east"):
                            orientable.setAxis(Axis.Z);
                            break;
                        case ("nether_portal_south"):
                            orientable.setAxis(Axis.X);
                            break;
                        case ("nether_portal_west"):
                            orientable.setAxis(Axis.Z);
                            break;
                    }
                    block.setBlockData(orientable);
                } else {
                    block.setType(Material.valueOf(getFillType().toUpperCase()));
                    if (getFillType().equals("water")) {
                        Levelled levelledData = (Levelled) block.getState().getBlockData();
                        levelledData.setLevel(6);
                        block.getState().setBlockData(levelledData);
                    }
                }


            }
        });
    }

    /**
     * Sets portal material to {@link Material#AIR}
     */
    public void clearPortal() {
        PortalRegion pr = new PortalRegion(getMinLoc(), getMaxLoc());
        pr.blockList().forEach(block -> {
            if (block.getType().toString().toLowerCase().equals(getFillType())) {
                block.setType(Material.AIR);
            }

        });
    }

    /**
     * Returns portal info as string
     *
     * @return info about the portal
     */
    public String toString() {
        String minLoc = getMinLoc().getWorld() + ":" + getMinLoc().getX() + ":" + getMinLoc().getY() + ":" + getMinLoc().getZ();
        String maxLoc = getMaxLoc().getX() + ":" + getMaxLoc().getY() + ":" + getMaxLoc().getZ();
        return getName() + ":" + getType() + ":" + getDestination() + ":" + getFillType() + ":" + minLoc + ":" + maxLoc;
    }

    /**
     * @return first corner of the portal
     */
    public Location getMinLoc() {
        return minLoc;
    }

    /**
     * @param minLoc first corner of the portal
     */
    public void setMinLoc(Location minLoc) {
        this.minLoc = minLoc;
    }

    /**
     * @return second corner of the portal
     */
    public Location getMaxLoc() {
        return maxLoc;
    }

    /**
     * @param maxLoc second corner of the portal
     */
    public void setMaxLoc(Location maxLoc) {
        this.maxLoc = maxLoc;
    }

    /**
     * @return {@link Material} used to fill the portal
     */
    public String getFillType() {
        return fillType;
    }

    /**
     * @param fillType {@link Material} used to fill the portal
     */
    public void setFillType(String fillType) {
        this.fillType = fillType;
    }

    /**
     * @return name of portal
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name of the portal
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return type of portal
     */

    public String getType() {
        return type;
    }

    /**
     * @param type type of the portal
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return destination of the portal
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination destination of the portal
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }
}
