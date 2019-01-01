package fi.matiaspaavilainen.masuiteportals.bungee;

import fi.matiaspaavilainen.masuitecore.core.Updator;
import fi.matiaspaavilainen.masuitecore.core.configuration.BungeeConfiguration;
import fi.matiaspaavilainen.masuitecore.core.database.ConnectionManager;
import fi.matiaspaavilainen.masuiteportals.core.Portal;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class MaSuitePortals extends Plugin {

    private BungeeConfiguration config = new BungeeConfiguration();
    private ConnectionManager cm = null;

    @Override
    public void onEnable() {
        super.onEnable();

        // Create configs
        config.create(this, "portals", "messages.yml");

        // Connect to database
        Configuration dbInfo = config.load(null, "config.yml");
        cm = new ConnectionManager(dbInfo.getString("database.table-prefix"), dbInfo.getString("database.address"), dbInfo.getInt("database.port"), dbInfo.getString("database.name"), dbInfo.getString("database.username"), dbInfo.getString("database.password"));
        cm.connect();

        // Create portals table
        cm.getDatabase().createTable("portals",
                "(id INT(10) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        "name VARCHAR(100) UNIQUE NOT NULL , " +
                        "server VARCHAR(100) NOT NULL, " +
                        "type VARCHAR(20) NOT NULL, " +
                        "destination VARCHAR(100) NOT NULL, " +
                        "filltype VARCHAR(100) NOT NULL, " +
                        "world VARCHAR(100) NOT NULL, " +
                        "minX DOUBLE NOT NULL, " +
                        "minY DOUBLE NOT NULL, " +
                        "minZ DOUBLE NOT NULL, " +
                        "maxX DOUBLE NOT NULL, " +
                        "maxY DOUBLE NOT NULL, " +
                        "maxZ DOUBLE NOT NULL); ");

        // Register PortalMessageListener
        getProxy().getPluginManager().registerListener(this, new PortalMessageListener(this));

        // Send portal lists to servers
        sendPortalList();

        // Updator
        new Updator(new String[]{getDescription().getVersion(), getDescription().getName(), "62434"}).checkUpdates();
    }

    /**
     * Sends portal list to servers
     */
    public void sendPortalList() {
        Portal p = new Portal();
        Set<Portal> portalSet = p.all();

        for (Map.Entry<String, ServerInfo> entry : getProxy().getServers().entrySet()) {
            ServerInfo serverInfo = entry.getValue();
            serverInfo.ping((result, error) -> {
                if (error == null) {
                    portalSet.forEach(portal -> {
                        if (serverInfo.getName().equals(portal.getServer())) {
                            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                                 DataOutputStream out = new DataOutputStream(b)) {
                                out.writeUTF("MaSuitePortals");
                                out.writeUTF("CreatePortal");
                                out.writeUTF(portal.toString());
                                serverInfo.sendData("BungeeCord", b.toByteArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        }

    }
}
