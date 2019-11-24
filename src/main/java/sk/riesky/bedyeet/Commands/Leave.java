package sk.riesky.bedyeet.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.riesky.bedyeet.GameTeam;
import sk.riesky.bedyeet.Manager;
import sk.riesky.bedyeet.Resources.Messenger;

import static sk.riesky.bedyeet.Resources.Messenger.Send;

public class Leave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Manager.getInstance().main_game != null) {
                if (args.length > 0) {
                    GameTeam team = Manager.getInstance().main_game.getTeam(args[0]);
                    team.removePlayer(player);
                    Send(player, String.format("Successfully left team %s", team.getDisplayName()), Messenger.MessageType.INFO);
                } else {
                    Send(player, "Please specify correct team.", Messenger.MessageType.WARNING);
                }
            } else {
                Send(player, "There is no game running.", Messenger.MessageType.WARNING);
            }
            return true;
        }
        return false;
    }
}
