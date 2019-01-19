package fi.matiaspaavilainen.masuiteportals.bukkit.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import fi.matiaspaavilainen.masuitecore.bukkit.chat.Formator;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
import fi.matiaspaavilainen.masuitecore.core.configuration.BukkitConfiguration;
import fi.matiaspaavilainen.masuiteportals.bukkit.MaSuitePortals;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {

    private MaSuitePortals plugin;

    private BukkitConfiguration config = new BukkitConfiguration();
    private Formator formator = new Formator();

    public SetCommand(MaSuitePortals p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length != 4) {
            formator.sendMessage(p, config.load("portals", "syntax.yml").getString("portal.set"));
            return false;
        }
        LocalSession localSession = plugin.we.getSession(p);
        if (localSession == null) {
            formator.sendMessage(p, config.load("portals", "messages.yml").getString("no-selected-area"));
            return false;
        }
        try {
            Region rg = localSession.getSelection(localSession.getSelectionWorld());
            if (rg == null) {
                formator.sendMessage(p, config.load("portals", "messages.yml").getString("no-selected-area"));
                return false;
            }
            if (Material.matchMaterial(args[3].toUpperCase()) == null && !args[3].contains("nether_portal")) {
                formator.sendMessage(p, config.load("portals", "messages.yml").getString("invalid-material"));
                return false;
            }
            String minLoc = p.getWorld().getName() + ":" + rg.getMinimumPoint().getBlockX() + ":" + rg.getMinimumPoint().getBlockY() + ":" + rg.getMinimumPoint().getBlockZ();
            String maxLoc = p.getWorld().getName() + ":" + rg.getMaximumPoint().getX() + ":" + rg.getMaximumPoint().getY() + ":" + rg.getMaximumPoint().getZ();
            new BukkitPluginChannel(plugin, p, new Object[]{"MaSuitePortals", "SetPortal", p.getName(), args[0], args[1], args[2], args[3], minLoc, maxLoc}).send();
        } catch (IncompleteRegionException e) {
            formator.sendMessage(p, config.load("portals", "messages.yml").getString("no-selected-area"));
            return false;
        }
        return true;
    }
}
