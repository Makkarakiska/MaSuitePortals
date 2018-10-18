package fi.matiaspaavilainen.masuiteportals;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PortalMessageListener implements Listener {

    private Configuration config = new Configuration();
    private Formator formator = new Formator();

    private MaSuitePortals plugin;

    public PortalMessageListener(MaSuitePortals p) {
        plugin = p;
    }

    @EventHandler
    public void onMessageReceived(PluginMessageEvent e) {
        if (e.getTag().equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                String subchannel = in.readUTF();
                if (subchannel.equals("MaSuitePortals")) {
                    String childchannel = in.readUTF();
                    if (childchannel.equals("SetPortal")) {
                        // Get the player
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(in.readUTF());
                        if (p == null) {
                            return;
                        }

                        // Check if player has permission
                        if (p.hasPermission("masuiteportals.portal.set")) {

                            // Setup portal
                            Portal portal = new Portal();
                            portal.setName(in.readUTF());
                            portal.setServer(p.getServer().getInfo().getName());
                            portal.setType(in.readUTF());
                            String destination = in.readUTF();

                            // Get portal type
                            if (portal.getType().equals("server")) {
                                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(destination);
                                if (serverInfo == null) {
                                    formator.sendMessage(p, config.load("portals", "messages.yml").getString("server-not-found"));
                                    return;
                                } else {
                                    portal.setDestination(destination);
                                }

                            } else if (portal.getType().equals("warp")) {
                                portal.setDestination(destination);
                            }
                            portal.setFillType(in.readUTF());
                            String[] minLoc = in.readUTF().split(":");
                            portal.setMinLoc(new Location(minLoc[0], Double.parseDouble(minLoc[1]), Double.parseDouble(minLoc[2]), Double.parseDouble(minLoc[3])));
                            String[] maxLoc = in.readUTF().split(":");
                            portal.setMaxLoc(new Location(maxLoc[0], Double.parseDouble(maxLoc[1]), Double.parseDouble(maxLoc[2]), Double.parseDouble(maxLoc[3])));

                            // Save the portal
                            if (portal.save()) {
                                formator.sendMessage(p, config.load("portals", "messages.yml").getString("portal.set"));
                            } else {
                                System.out.println("[MaSuite] [Portals] There was an error during saving process.");
                            }
                            plugin.sendPortalList();
                        } else {
                            formator.sendMessage(p, config.load("portals", "messages.yml").getString("no-permission"));
                        }
                    }

                    if (childchannel.equals("DelPortal")) {
                        // Get the player
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(in.readUTF());
                        if (p == null) {
                            return;
                        }

                        // If player has permission
                        if (p.hasPermission("masuiteportals.portal.delete")) {
                            Portal portal = new Portal().find(in.readUTF());

                            // If portal is null, return not found message
                            if (portal == null) {
                                formator.sendMessage(p, config.load("portals", "messages.yml").getString("portal.not-found"));
                                return;
                            }

                            // If delete successful, info
                            if (portal.delete()) {
                                formator.sendMessage(p, config.load("portals", "messages.yml").getString("portal.deleted"));
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("MaSuitePortals");
                                out.writeUTF("DeletePortal");
                                out.writeUTF(portal.getName());
                                ProxyServer.getInstance().getServerInfo(portal.getServer()).sendData("BungeeCord", out.toByteArray());
                            } else {
                                System.out.println("[MaSuite] [Portals] There was an error during deleting process.");
                            }
                        }else {
                            formator.sendMessage(p, config.load("portals", "messages.yml").getString("no-permission"));
                        }
                    }
                    if (childchannel.equals("RequestPortals")) {
                        plugin.sendPortalList();
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
