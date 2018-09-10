package fi.matiaspaavilainen.masuiteportals;

import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuiteportals.commands.Delete;
import fi.matiaspaavilainen.masuiteportals.commands.List;
import fi.matiaspaavilainen.masuiteportals.commands.Set;
import net.md_5.bungee.api.plugin.Plugin;



public class MaSuitePortals extends Plugin {

    @Override
    public void onEnable() {
        super.onEnable();

        // Create configs
        Configuration config = new Configuration();
        config.create(this, "portals", "messages.yml");
        config.create(this, "portals", "syntax.yml");

        // Register commands
        getProxy().getPluginManager().registerCommand(this, new Set());
        getProxy().getPluginManager().registerCommand(this, new Delete());
        getProxy().getPluginManager().registerCommand(this, new List());
    }
}
