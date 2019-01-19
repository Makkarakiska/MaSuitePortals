package fi.matiaspaavilainen.masuiteportals.bukkit.commands;

import fi.matiaspaavilainen.masuitecore.bukkit.chat.Formator;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
import fi.matiaspaavilainen.masuitecore.core.configuration.BukkitConfiguration;
import fi.matiaspaavilainen.masuiteportals.bukkit.MaSuitePortals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand implements CommandExecutor {

    private MaSuitePortals plugin;


    public DeleteCommand(MaSuitePortals p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        BukkitConfiguration config = new BukkitConfiguration();
        Player p = (Player) sender;
        if (args.length != 1) {
            new Formator().sendMessage(p, config.load("portals", "syntax.yml").getString("portal.delete"));
            return false;
        }
        new BukkitPluginChannel(plugin, p, new Object[]{"MaSuitePortals", "DelPortal", p.getName(), args[0]}).send();
        return false;
    }
}
