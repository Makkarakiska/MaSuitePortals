package fi.matiaspaavilainen.masuiteportals.bukkit.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import fi.matiaspaavilainen.masuitecore.bukkit.chat.Formator;
import fi.matiaspaavilainen.masuitecore.core.configuration.BukkitConfiguration;
import fi.matiaspaavilainen.masuiteportals.bukkit.MaSuitePortals;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Set implements CommandExecutor {

    private MaSuitePortals plugin;

    private BukkitConfiguration config;
    private Formator formator = new Formator();

    public Set(MaSuitePortals p) {
        plugin = p;
        config = new BukkitConfiguration(plugin);
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
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                out.writeUTF("MaSuitePortals");
                out.writeUTF("SetPortal");
                out.writeUTF(p.getName()); // Creator's name
                out.writeUTF(args[0]); // Portal name
                out.writeUTF(args[1]); // Portal type
                out.writeUTF(args[2]); // Portal destination
                out.writeUTF(args[3]); // Portal fill type
                out.writeUTF(p.getWorld().getName() + ":" + rg.getMinimumPoint().getBlockX() + ":" + rg.getMinimumPoint().getBlockY() + ":" + rg.getMinimumPoint().getBlockZ()); // Portal min loc
                out.writeUTF(p.getWorld().getName() + ":" + rg.getMaximumPoint().getX() + ":" + rg.getMaximumPoint().getY() + ":" + rg.getMaximumPoint().getZ()); // Portal max loc
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.getStackTrace();
            }
        } catch (IncompleteRegionException e) {
            formator.sendMessage(p, config.load("portals", "messages.yml").getString("no-selected-area"));
            return false;
        }
        return true;
    }
}
