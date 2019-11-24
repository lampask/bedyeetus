package sk.riesky.bedyeet.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.riesky.bedyeet.*;
import sk.riesky.bedyeet.Events.GameStartEvent;
import sk.riesky.bedyeet.Resources.Constants;
import sk.riesky.bedyeet.Resources.Messenger;
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
                } else if (args[0].equals("start")) {
                    if (args.length == 2) {
                        Game g = Manager.getInstance().getGames().get(args[1]);
                        g.setState(Manager.GameState.IN_PROGRESS);
                        Manager.getInstance().main_game = g;
                        g.mapInit();
                        Send(player, String.format("Starting game %s", g.getDisplay_name()));
                        for (GameTeam team: g.getTeams()) {
                            for (GamePlayer p: team.getPlayers()) {
                                p.player.teleport(g.spawn_location);
                                p.player.getInventory().clear();
                                p.player.setGameMode(GameMode.SURVIVAL);
                            }
                        }
                        countdown();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.main, () -> {
                            main.getServer().getPluginManager().callEvent(new GameStartEvent());
                            Bukkit.broadcastMessage("Game started!");
                        }, 10000/50);
                    } else {
                        Send(player, "Please specify game name!", MessageType.WARNING);
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
                                target.removeTeam(team, true);
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
                    Game target = manager.getEditedTarget(player.getUniqueId());
                    if (target != null) {
                        if (target.getEditorTool() != Manager.GameEditorTool.TEAM) {
                            if (args[0].equals("diamond")) {
                                target.setEditorTool(Manager.GameEditorTool.DIAMOND_GENERATOR);
                                Messenger.Send(player, "Please select position for diamond spawner.", MessageType.INFO, PrefixType.EDITOR);
                            } else if (args[0].equals("emerald")) {
                                target.setEditorTool(Manager.GameEditorTool.EMERALD_GENERATOR);
                                Messenger.Send(player, "Please select position for emerald spawner.", MessageType.INFO, PrefixType.EDITOR);
                            } else if (args[0].equals("deletespawner")) {
                                target.setEditorTool(Manager.GameEditorTool.DELETE);
                                Messenger.Send(player, "Please select spawner to delete.", MessageType.INFO, PrefixType.EDITOR);
                            } else if (args[0].equals("complete")) {
                                if (target.getTeams().size() > -1) {
                                    target.setState(Manager.GameState.PREPARED);
                                    Manager.getInstance().unsetFrom_EditMode(player.getUniqueId());
                                    Messenger.Send(player, "GAME COMPLETED", MessageType.ANNOUNCE);
                                } else {
                                    Messenger.Send(player, "You need to have at least 2 teams to complete game.", MessageType.ERROR, PrefixType.EDITOR);
                                }
                            }
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

    BukkitTask task;
    int cd = 10;
    void countdown() {
        task = Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(String.format("%s", cd));
                cd--;
                if (cd == 1) {
                    cd = 10;
                    task.cancel();
                } else {
                    task = Bukkit.getScheduler().runTaskLater(main, this, 1000 / 50);
                }
            }
        }, 1000/50);
    }
}
