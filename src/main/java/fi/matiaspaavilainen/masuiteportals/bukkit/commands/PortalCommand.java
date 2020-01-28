package fi.matiaspaavilainen.masuiteportals.bukkit.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import fi.matiaspaavilainen.masuitecore.acf.BaseCommand;
import fi.matiaspaavilainen.masuitecore.acf.annotation.*;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
import fi.matiaspaavilainen.masuitecore.core.objects.Location;
import fi.matiaspaavilainen.masuiteportals.bukkit.MaSuitePortals;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PortalCommand extends BaseCommand {

    private MaSuitePortals plugin;

    public PortalCommand(MaSuitePortals plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("setportal|createportal|portalset")
    @CommandPermission("masuiteportals.portal.set")
    @Description("Creates a new portal or updates existing portal")
    @CommandCompletion("@portals warp|server * *")
    public void setPortalCommand(Player player, @Single String name, @Single String type, @Single String destination, @Single String fillType) {

        LocalSession localSession = plugin.we.getSession(player);
        if (localSession == null) {
            plugin.formator.sendMessage(player, plugin.config.load("portals", "messages.yml").getString("no-selected-area"));
            return;
        }
        try {
            Region rg = localSession.getSelection(localSession.getSelectionWorld());
            if (rg == null) {
                plugin.formator.sendMessage(player, plugin.config.load("portals", "messages.yml").getString("no-selected-area"));
                return;
            }
            if (Material.matchMaterial(fillType.toUpperCase()) == null && !fillType.contains("nether_portal")) {
                plugin.formator.sendMessage(player, plugin.config.load("portals", "messages.yml").getString("invalid-material"));
                return;
            }
            String minLoc = new Location(player.getWorld().getName(), (double) rg.getMinimumPoint().getBlockX(), (double) rg.getMinimumPoint().getBlockY(), (double) rg.getMinimumPoint().getBlockZ()).serialize();
            String maxLoc = new Location(player.getWorld().getName(), (double) rg.getMaximumPoint().getBlockX(), (double) rg.getMaximumPoint().getBlockY(), (double) rg.getMaximumPoint().getBlockZ()).serialize();
            new BukkitPluginChannel(plugin, player, "MaSuitePortals", "SetPortal", player.getName(), name, type, destination, fillType, minLoc, maxLoc).send();
        } catch (IncompleteRegionException e) {
            plugin.formator.sendMessage(player, plugin.config.load("portals", "messages.yml").getString("no-selected-area"));
        }
    }

    @CommandAlias("delportal|deleteportal|portaldel")
    @CommandPermission("masuiteportals.portal.delete")
    @Description("Deletes existing portal")
    @CommandCompletion("@portals")
    public void deletePortalCommand(Player player, @Single String name) {
        new BukkitPluginChannel(plugin, player, "MaSuitePortals", "DelPortal", player.getName(), name).send();
    }

    @CommandAlias("portals|portalslist|listportals")
    @CommandPermission("masuiteportals.portal.delete")
    @Description("List all portals")
    public void listPortalsCommand(Player player) {
        new BukkitPluginChannel(plugin, player, "MaSuitePortals", "ListCommand", player.getName()).send();
    }
}
