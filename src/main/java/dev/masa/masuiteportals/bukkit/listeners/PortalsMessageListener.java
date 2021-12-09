package dev.masa.masuiteportals.bukkit.listeners;

import dev.masa.masuiteportals.bukkit.MaSuitePortals;
import dev.masa.masuiteportals.core.models.Portal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

public class PortalsMessageListener implements PluginMessageListener {

    private MaSuitePortals plugin;

    public PortalsMessageListener(MaSuitePortals p) {
        plugin = p;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = null;
        try {
            subchannel = in.readUTF();
            if (subchannel.equals("MaSuitePortals")) {
                String childchannel = in.readUTF();

                // If channel is CreatePortal
                if (childchannel.equals("CreatePortal")) {
                    Portal portal = new Portal().deserialize(in.readUTF());
                    // Split portal information
                    // If portal's world is not null
                    if (Bukkit.getWorld(portal.getMinLocation().getWorld()) != null) {
                        this.plugin.getLogger().log(Level.INFO, "Adding portal " + portal.serialize());
                        plugin.portalManager.addPortal(portal);
                        plugin.portalManager.fillPortal(portal);
                    } else {
                        // Return warning message
                        System.out.println("[MaSuite] [Portals] [Portal=" + portal.getName() + "] [World=" + portal.getMinLocation().getWorld() + "] World not found");
                    }
                }

                // If channel is DeletePortal
                if (childchannel.equals("DeletePortal")) {
                    //Check if list contains the name
                    Portal portal = plugin.portalManager.getPortal(in.readUTF());
                    plugin.portalManager.removePortal(portal);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}