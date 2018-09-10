package fi.matiaspaavilainen.masuiteportals.commands;

import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Set extends Command {
    public Set() {
        super("setportal", "masuiteportals.portal.set", "portalset", "createportal");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(!(cs instanceof ProxiedPlayer)){
            return;
        }
        Formator formator = new Formator();
        Configuration config = new Configuration();
        ProxiedPlayer p = (ProxiedPlayer) cs;
        if(args.length == 4){
            // Create portal
        }else{
            formator.sendMessage(p, config.load("portals", "syntax.yml").getString("portal.set"));
        }
    }
}
