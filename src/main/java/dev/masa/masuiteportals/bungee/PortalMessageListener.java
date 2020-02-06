package dev.masa.masuiteportals.bungee;

import dev.masa.masuitecore.bungee.chat.Formator;
import dev.masa.masuitecore.core.channels.BungeePluginChannel;
import dev.masa.masuitecore.core.configuration.BungeeConfiguration;
import dev.masa.masuitecore.core.objects.Location;
import dev.masa.masuiteportals.core.models.Portal;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.StringJoiner;

public class PortalMessageListener implements Listener {

    private BungeeConfiguration config = new BungeeConfiguration();
    private Formator formator = new Formator();

    private MaSuitePortals plugin;

    public PortalMessageListener(MaSuitePortals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessageReceived(PluginMessageEvent e) {
        if (e.getTag().equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                // Get subchannel
                String subchannel = in.readUTF();
                if (subchannel.equals("MaSuitePortals")) {
                    String childchannel = in.readUTF();

                    // If childchannel equals SetPortal
                    if (childchannel.equals("SetPortal")) {
                        // Get the player
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(in.readUTF());
                        if (p == null) {
                            return;
                        }

                        // Setup portal
                        Portal portal = new Portal();
                        portal.setName(in.readUTF());

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
                        portal.setMinLoc(new Location().deserialize(in.readUTF()));
                        portal.setMaxLoc(new Location().deserialize(in.readUTF()));

                        portal.getMinLoc().setServer(p.getServer().getInfo().getName());
                        portal.getMaxLoc().setServer(p.getServer().getInfo().getName());

                        // Save the portal
                        if (plugin.portalService.createPortal(portal)) {
                            formator.sendMessage(p, config.load("portals", "messages.yml").getString("portal.set").replace("%name%", portal.getName()).replace("%destination%", portal.getDestination()));
                            new BungeePluginChannel(plugin, plugin.getProxy().getServerInfo(portal.getMinLoc().getServer()), "MaSuitePortals", "CreatePortal", portal.serialize()).send();
                        } else {
                            System.out.println("[MaSuite] [Portals] There was an error during saving process.");
                        }
                                            }

                    if (childchannel.equals("DelPortal")) {
                        // Get the player
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(in.readUTF());
                        if (p == null) {
                            return;
                        }
                        Portal portal = plugin.portalService.getPortal(in.readUTF());

                        // If portal is null, return not found message
                        if (portal == null) {
                            formator.sendMessage(p, config.load("portals", "messages.yml").getString("portal.not-found"));
                            return;
                        }

                        // If delete successful, info
                        if (plugin.portalService.removePortal(portal)) {
                            formator.sendMessage(p, config.load("portals", "messages.yml").getString("portal.deleted").replace("%name%", portal.getName()));
                            new BungeePluginChannel(plugin, plugin.getProxy().getServerInfo(portal.getMinLoc().getServer()), "MaSuitePortals", "DeletePortal", portal.getName()).send();
                        } else {
                            System.out.println("[MaSuite] [Portals] There was an error during deleting process.");
                        }
                    }
                    if (childchannel.equals("ListCommand")) {
                        // Get the player
                        ProxiedPlayer player = plugin.getProxy().getPlayer(in.readUTF());
                        if (player == null) {
                            return;
                        }

                        String name = config.load("portals", "messages.yml").getString("portal.list.name");
                        StringJoiner list = new StringJoiner(config.load("portals", "messages.yml").getString("portal.list.splitter"));

                        // Loop every portal and add items to list
                        plugin.portalService.getAllPortals().forEach(portal -> {
                            if (portal.getMinLoc().getServer().equals(player.getServer().getInfo().getName())) {
                                list.add(name.replace("%portal%", portal.getName()));
                            }
                        });

                        formator.sendMessage(player, config.load("portals", "messages.yml").getString("portal.list.title") + list);
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
