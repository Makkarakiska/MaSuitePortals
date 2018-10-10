package fi.matiaspaavilainen.masuiteportals;

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
                            }else{
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
                        portal.save();
                    }
                    if(childchannel.equals("SendPlayer")){
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(in.readUTF());
                        if (p == null) {
                            return;
                        }
                        Portal portal = new Portal();
                        p.connect(ProxyServer.getInstance().getServerInfo("freebuild"));
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
