package fi.matiaspaavilainen.masuiteportals.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Delete extends Command {
    public Delete() {
        super("delportal", "masuiteportals.portal.delete", "portaldel", "deleteportal");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

    }
}
