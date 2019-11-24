package sk.riesky.bedyeet.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.riesky.bedyeet.Game;
import sk.riesky.bedyeet.GameTeam;
import sk.riesky.bedyeet.Manager;
import sk.riesky.bedyeet.Resources.Messenger;

import static sk.riesky.bedyeet.Resources.Messenger.*;

public class Join implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 1) {
                Game game = Manager.getInstance().getGames().get(args[0]);
                GameTeam team = game.getTeam(args[1]);
                if (team != null) {
                    player.setDisplayName(team.getColor()+player.getDisplayName());
                    team.addPlayer(player);
                } else {
                    Send(player, "Team entered is incorrect.", MessageType.WARNING);
                }
                Send(player, String.format("Successfully joined team %s (%s)", team.getDisplayName()+ChatColor.RESET, game.getDisplay_name()+ChatColor.RESET), MessageType.INFO);
            } else {
                Send(player, "Please specify correct game and team.", MessageType.WARNING);
            }
            return true;
        }
        return false;
    }
}
