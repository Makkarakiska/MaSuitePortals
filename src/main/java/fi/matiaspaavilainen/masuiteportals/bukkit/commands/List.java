package fi.matiaspaavilainen.masuiteportals.bukkit.commands;

import fi.matiaspaavilainen.masuitecore.bukkit.chat.Formator;
import fi.matiaspaavilainen.masuitecore.core.configuration.BukkitConfiguration;
import fi.matiaspaavilainen.masuitecore.core.objects.PluginChannel;
import fi.matiaspaavilainen.masuiteportals.bukkit.MaSuitePortals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class List implements CommandExecutor {

    private MaSuitePortals plugin;

    public List(MaSuitePortals plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length != 0) {
            new Formator().sendMessage(p, new BukkitConfiguration().load("portals", "syntax.yml").getString("portal.list"));
            return false;
        }
        new PluginChannel(plugin, p, new Object[]{"MaSuitePortals", "List", p.getName()}).send();
        return false;
    }
}
