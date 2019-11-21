package sk.riesky.bedyeet.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.riesky.bedyeet.Bedyeetus;
import sk.riesky.bedyeet.Game;
import sk.riesky.bedyeet.GameTeam;
import sk.riesky.bedyeet.Manager;
import sk.riesky.bedyeet.Resources.Constants;
import sk.riesky.bedyeet.Resources.Messenger.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sk.riesky.bedyeet.Resources.Messenger.Send;

public class MainCommand implements CommandExecutor {

    private final Bedyeetus main;
    private Manager manager = Manager.getInstance();
    ArrayList<String> list_aliases = new ArrayList<>(List.of("games", "list"));

    public MainCommand(Bedyeetus _main) {
        this.main = _main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equals("create")) {
                    if (args.length == 2) {
                        if (!manager.isEditing(player.getUniqueId())) {
                            Game new_game = new Game(this.main, player, args[1]);
                            main.getServer().getPluginManager().registerEvents(new_game, main);
                            manager.addGame(new_game);
                        } else {
                            Send(player, "You are already in edit mode!", MessageType.WARNING);
                        }
                    } else {
                        Send(player, "Please specify game name!", MessageType.WARNING);
                    }
                } else if (list_aliases.contains(args[0])) {
                    var games = manager.getGames();
                    if (games.size() > 0) {
                        Send(player, "List of created games:", MessageType.INFO);
                        for (Map.Entry<String, Game> entry: games.entrySet()) {
                            Send(player, "┗ "+entry.getValue().getDisplay_name(), MessageType.INFO, PrefixType.NONE);
                        }
                    } else {
                        Send(player, "There aren't any games created yet.", MessageType.WARNING);
                    }
                }
                if (manager.isEditing(player.getUniqueId())) {
                    if (args[0].equals("exit")) {
                        if (manager.isEditing(player.getUniqueId())) {
                            manager.unsetFrom_EditMode(player.getUniqueId());
                        } else {
                            Send(player, "You are currently not in edit mode!", MessageType.ANNOUNCE);
                        }
                    } else if (args[0].equals("addteam")) {
                        if (args.length == 2) {
                            Game target = manager.getEditedTarget(player.getUniqueId());
                            GameTeam new_team = new GameTeam(this.main, player, target, args[1]);
                            this.main.getServer().getPluginManager().registerEvents(new_team, main);
                            target.addTeam(new_team);
                        } else {
                            Send(player, "Please specify team name!", MessageType.WARNING, PrefixType.EDITOR);
                        }
                    } else if (args[0].equals("removeteam")) {
                        if (args.length == 2) {
                            Game target = manager.getEditedTarget(player.getUniqueId());
                            GameTeam team = target.getTeam(args[1]);
                            if (team != null) {
                                target.removeTeam(team);
                            } else {
                                Send(player, "Team was not found!", MessageType.ERROR, PrefixType.EDITOR);
                            }
                        } else {
                            Send(player, "Please specify team name!", MessageType.WARNING, PrefixType.EDITOR);
                        }
                    } else if (args[0].equals("teams")) {
                        var teams = manager.getEditedTarget(player.getUniqueId()).getTeams();
                        if (teams.size() > 0) {
                            Send(player, "List of created teams:", MessageType.INFO, PrefixType.EDITOR);
                            for (GameTeam team: teams) {
                                Send(player, "┗ "+team.getDisplayName(), MessageType.INFO, PrefixType.NONE);
                            }
                        } else {
                            Send(player, "There aren't any teams created yet.", MessageType.WARNING, PrefixType.EDITOR);
                        }
                    }
                    if (manager.getEditedTarget(player.getUniqueId()).getEditorTool() != Manager.GameEditorTool.TEAM) {
                        if (args[0].equals("diamond")) {

                        }
                    }
                }
            }
        }
        if (args.length == 0) {
            // No parameters --> version information
            Bukkit.broadcastMessage(String.format("Character test: %s", Constants.CHARS));
            Send(sender, String.format("Running version %s of %s", main.getDescription().getVersion(), main.getDescription().getName()), MessageType.INFO);
        } else if (args.length > 0) {
            if (args[0].equals("delete")) {
                if (args.length == 2) {
                    if (manager.getGames().containsKey(args[1])) {
                        manager.removeGame(sender, args[1]);
                    } else {
                        Send(sender, "Can't find the game specified.", MessageType.ERROR);
                    }
                } else {
                    Send(sender, "Please specify game name!", MessageType.WARNING);
                }
            }
        }
        return true;
    }
}
