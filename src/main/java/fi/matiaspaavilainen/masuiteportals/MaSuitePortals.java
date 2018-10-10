package fi.matiaspaavilainen.masuiteportals;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuiteportals.database.Database;
import fi.matiaspaavilainen.masuitecore.Updator;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

public class MaSuitePortals extends Plugin {

    public static Database db = new Database();

    @Override
    public void onEnable() {
        super.onEnable();

        // Create configs
        Configuration config = new Configuration();
        config.create(this, "portals", "messages.yml");
        config.create(this, "portals", "syntax.yml");

        // Connect to database
        db.connect();

        // Create portals table
        db.createTable("portals",
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
        // TODO: Add ID after update
        // new Updator().checkVersion(this.getDescription(), "");
    }

    public void sendPortalList(){
        Portal p = new Portal();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (Map.Entry<String, ServerInfo> entry : getProxy().getServers().entrySet()) {
            ServerInfo serverInfo = entry.getValue();
            serverInfo.ping((result, error) -> {
                if (error == null) {
                    StringBuilder portals = new StringBuilder();
                    p.all().forEach(portal -> {
                        if(serverInfo.getName().equals(portal.getServer())){
                            portals.append(portal.toString()).append("--");
                        }
                    });
                    out.writeUTF("MaSuitePortals");
                    out.writeUTF("PortalList");
                    out.writeUTF(portals.toString());
                    System.out.println(portals.toString());
                    serverInfo.sendData("BungeeCord", out.toByteArray());
                }
            });
        }
    }
}
