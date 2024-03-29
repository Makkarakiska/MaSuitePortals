package dev.masa.masuiteportals.bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import dev.masa.masuitecore.acf.PaperCommandManager;
import dev.masa.masuitecore.bukkit.chat.Formator;
import dev.masa.masuitecore.core.Updator;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import dev.masa.masuitecore.core.configuration.BukkitConfiguration;
import dev.masa.masuiteportals.bukkit.commands.PortalCommand;
import dev.masa.masuiteportals.bukkit.listeners.MovementListener;
import dev.masa.masuiteportals.bukkit.listeners.PhysicsListener;
import dev.masa.masuiteportals.bukkit.listeners.PortalsMessageListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MaSuitePortals extends JavaPlugin implements Listener {

    public WorldEditPlugin we = null;

    public BukkitConfiguration config = new BukkitConfiguration();
    public Formator formator = new Formator();

    public PortalManager portalManager = new PortalManager(this);

    @Override
    public void onEnable() {
        //Create configs
        config.create(this, "portals", "syntax.yml");
        config.create(this, "portals", "config.yml");
        config.create(this, "portals", "messages.yml");

        // Load WorldEdit
        we = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        if (we == null) {
            System.out.println("[MaSuitePortals] WorldEdit not detected. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

        new Updator(getDescription().getVersion(), getDescription().getName(), "62434").checkUpdates();

        // Register and load everything
        registerListener();

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new PortalCommand(this));
        manager.getCommandCompletions().registerCompletion("portals", c -> {
            List<String> portalNames = new ArrayList<>();
            portalManager.portals.values().forEach(portal -> portalNames.add(portal.getName()));
            return portalNames;
        });

        portalManager.loadPortals();
    }

    @Override
    public void onDisable() {
        portalManager.portals.values().forEach(portal -> portalManager.clearPortal(portal));
    }

    /**
     * Register listener
     */
    private void registerListener() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PortalsMessageListener(this));

        getServer().getPluginManager().registerEvents(new MovementListener(this), this);

        if(this.config.load("portals", "config.yml").getBoolean("cancel-block-physics")) {
            getServer().getPluginManager().registerEvents(new PhysicsListener(this), this);
        }

        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // If list is empty when player joins, request portals
        if (portalManager.portals.isEmpty()) {
            getServer().getScheduler().runTaskLaterAsynchronously(this, () -> new BukkitPluginChannel(this, e.getPlayer(), "MaSuitePortals", "RequestPortals").send(), 100);
        }
    }

}
