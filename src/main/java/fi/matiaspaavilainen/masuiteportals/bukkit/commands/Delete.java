package fi.matiaspaavilainen.masuiteportals.bukkit.commands;

import fi.matiaspaavilainen.masuitecore.bukkit.chat.Formator;
import fi.matiaspaavilainen.masuitecore.core.configuration.BukkitConfiguration;
import fi.matiaspaavilainen.masuiteportals.bukkit.MaSuitePortals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Delete implements CommandExecutor {

    private MaSuitePortals plugin;


    public Delete(MaSuitePortals p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        BukkitConfiguration config = new BukkitConfiguration(plugin);
        Player p = (Player) sender;
        if (args.length != 1) {
            new Formator().sendMessage(p, config.load("portals", "syntax.yml").getString("portal.delete"));
            return false;
        }

        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            out.writeUTF("MaSuitePortals");
            out.writeUTF("DelPortal");
            out.writeUTF(p.getName()); // Creator's name
            out.writeUTF(args[0]); // Portal name
            p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.getStackTrace();
        }

        return false;
    }
}
