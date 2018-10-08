package fi.matiaspaavilainen.masuiteportals;

import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuiteportals.database.Database;
import fi.matiaspaavilainen.masuiteportals.commands.Delete;
import fi.matiaspaavilainen.masuiteportals.commands.List;
import fi.matiaspaavilainen.masuiteportals.commands.Set;
import fi.matiaspaavilainen.masuitecore.Updator;
import net.md_5.bungee.api.plugin.Plugin;

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
        getProxy().getPluginManager().registerListener(this, new PortalMessageListener());

        // Updator
        // TODO: Add ID after update
        // new Updator().checkVersion(this.getDescription(), "");
    }
}
