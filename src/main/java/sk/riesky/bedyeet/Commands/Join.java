package sk.riesky.bedyeet.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.riesky.bedyeet.Manager;
import sk.riesky.bedyeet.Resources.Messenger;

import static sk.riesky.bedyeet.Resources.Messenger.*;

public class Join implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.setDisplayName(player.getName());
            Send(player, "Succesfully", MessageType.INFO);
            return true;
        }
        return false;
    }
}
