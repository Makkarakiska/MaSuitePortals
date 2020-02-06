package dev.masa.masuiteportals.bungee;

import dev.masa.masuitecore.core.Updator;
import dev.masa.masuitecore.core.channels.BungeePluginChannel;
import dev.masa.masuitecore.core.configuration.BungeeConfiguration;
import dev.masa.masuiteportals.core.services.PortalService;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

public class MaSuitePortals extends Plugin {

    private BungeeConfiguration config = new BungeeConfiguration();
    public PortalService portalService;

    @Override
    public void onEnable() {
        super.onEnable();

        // Create configs
        config.create(this, "portals", "messages.yml");

        portalService = new PortalService(this);
        // Create portals table
        // Register PortalMessageListener
        getProxy().getPluginManager().registerListener(this, new PortalMessageListener(this));

        portalService.initializePortals();
        // Send portal lists to servers
        sendPortalList();

        // Updator
        new Updator(getDescription().getVersion(), getDescription().getName(), "62434").checkUpdates();
    }

    /**
     * Sends portal list to servers
     */
    public void sendPortalList() {
        for (Map.Entry<String, ServerInfo> entry : getProxy().getServers().entrySet()) {
            ServerInfo serverInfo = entry.getValue();
            serverInfo.ping((result, error) -> {
                if (error == null) {
                    portalService.getAllPortals().forEach(portal -> {
                        if (serverInfo.getName().equals(portal.getMinLoc().getServer())) {
                            new BungeePluginChannel(this, serverInfo, "MaSuitePortals", "CreatePortal", portal.serialize()).send();
                        }
                    });
                }
            });
        }

    }
}
