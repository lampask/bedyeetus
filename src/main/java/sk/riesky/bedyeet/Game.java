package sk.riesky.bedyeet;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import sk.riesky.bedyeet.Classes.DiamondSpawner;
import sk.riesky.bedyeet.Classes.EmeraldSpawner;
import sk.riesky.bedyeet.Classes.Spawner;
import sk.riesky.bedyeet.Events.GameStartEvent;
import sk.riesky.bedyeet.Manager.GameEditorTool;
import sk.riesky.bedyeet.Manager.GameState;
import sk.riesky.bedyeet.Resources.Constants;
import sk.riesky.bedyeet.Resources.Database;
import sk.riesky.bedyeet.Resources.Messenger;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static sk.riesky.bedyeet.Resources.Messenger.*;

public class Game implements Listener {

    private final Bedyeetus main;
    String name;
    private Player creator;
    private ArrayList<GameTeam> teams = new ArrayList<>();
    GameState state = GameState.EDITING;
    Scoreboard scoreboard;
    private GameEditorTool editing;
    public Location spawn_location;
    private ArrayList<Spawner> global_spawners = new ArrayList<>();
    private World map;


    public Game(Bedyeetus _main, Player _creator, String _name) {
        this.main = _main;
        this.name = _name; // Define the name of game
        this.creator = _creator;
        //this.state = GameState.EDITING; // Set completion state to (NOT COMPLETED) - editing
        Manager.getInstance().setTo_EditMode(creator.getUniqueId(), this.name); // Mark creator as editor
        Send(creator, String.format("Game %s created in world '%s'", this.name, creator.getWorld().getName()), MessageType.INFO, PrefixType.EDITOR); // Send creation notice
        ItemStack wand = Manager.getWand();
        if (!creator.getInventory().contains(wand)) {
            creator.getInventory().addItem(Manager.getWand()); // Give creator selection item
        }
        // SET SOME GLOBAL PARAMETERS
        Messenger.Send(this.creator, "Please select spawn position for this game.", MessageType.INFO, PrefixType.EDITOR);
        this.editing = GameEditorTool.SPAWN_LOC;

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("teams", "dummy", "Teams");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Game(Bedyeetus _main, Player _creator, String _name, Location spawn_location, ArrayList<Spawner> spawners, ArrayList<GameTeam> teams) {
        this.main = _main;
        this.name = _name; // Define the name of game
        this.creator = _creator;
        this.spawn_location = spawn_location;
        this.global_spawners = spawners;
        this.teams = teams;
        this.main.getLogger().info(String.format("Game %s loaded form database", this.name));
    }

    public void Save() {
        Integer id = null;
        try(PreparedStatement s = Database.getInstance().get().prepareStatement("INSERT INTO games (name, world, x, y, z) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, this.name);
            s.setString(2, this.spawn_location.getWorld().getName());
            s.setDouble(3, this.spawn_location.getX());
            s.setDouble(4, this.spawn_location.getY());
            s.setDouble(5, this.spawn_location.getZ());

            int affectedRows = s.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = s.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Spawner sp: this.global_spawners) {
            try(PreparedStatement s = Database.getInstance().get().prepareStatement("INSERT INTO spawners (type, game_id, x, y, z) VALUES (?, ?, ?, ?, ?)")) {
                s.setString(1, sp.getBlock().toString());
                s.setInt(2, id);
                s.setDouble(3, sp.getLocation().getX());
                s.setDouble(4, sp.getLocation().getY());
                s.setDouble(5, sp.getLocation().getZ());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Send(this.creator, String.format("(:%s) Game successfully saved!", id), MessageType.EDIT);
    }

    public String getDisplay_name() {
        return Constants.STATE_COLOR.get(state)+name+ChatColor.RESET;
    }

    public void addTeam(GameTeam t) {
        if (this.teams.size() < 1) {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = scoreboard.registerNewObjective("teams", "dummy", "Teams");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        this.teams.add(t);
    }

    public void start() {
        Manager.getInstance().main_game = this;
    }

    public Scoreboard getBoard() {
        return this.scoreboard;
    }

    public void removeTeam(GameTeam t) {
        this.teams.get(this.teams.indexOf(t)).delete();
        if (this.teams.size() < 1) {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
    }

    public void removeTeam(GameTeam t, boolean all) {
        this.teams.get(this.teams.indexOf(t)).delete();
        if (all) {
            this.teams.get(this.teams.indexOf(t));
        }
    }

    public void mapInit() {
        String name = "temp";
        this.copyWorld(this.spawn_location.getWorld(), name);
        this.map = Bukkit.getWorld(name);
    }

    public void delete() {
        for (GameTeam t: this.teams) {
            this.removeTeam(t);
        }
        this.teams.clear();
    }

    public GameTeam getTeam(String name) {
        for (GameTeam t: this.teams) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<GameTeam> getTeams() {
        return this.teams;
    }

    public void setEditorTool (GameEditorTool tool) {
        this.editing = tool;
    }

    public GameEditorTool getEditorTool() {
        return this.editing;
    }

    public void setState (GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return this.state;
    }

    private void SendEditorMsg(String thing, Location l) {
        Send(this.creator, String.format("%s position was set to: x: %s  y: %s  z: %s", thing, l.getX(), l.getY(), l.getZ()), MessageType.EDIT, PrefixType.EDITOR);
    }

    private boolean locFree(Location l) {
        for (Spawner generator: this.global_spawners) {
            if (generator.getLocation() == l) {
                return false;
            }
        }
        return true;
    }

    private Integer getGeneratorIndex(Location l) {
        for (Spawner generator: this.global_spawners) {
            if (generator.getLocation() == l) {
                return global_spawners.indexOf(generator);
            }
        }
        return null;
    }

    public static void copyWorld(World originalWorld, String newWorldName) {
        copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        new WorldCreator(newWorldName).createWorld();
    }

    private static void copyFileStructure(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void OnEdit(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                if (player.getInventory().getItemInMainHand().equals(Manager.getWand())) {
                    if (this.state == GameState.EDITING) {
                        if (this.getEditorTool() != GameEditorTool.INACTIVE) {
                            Location clicked = Objects.requireNonNull(event.getClickedBlock()).getLocation();
                            if (this.editing == GameEditorTool.DELETE) {
                                Integer remove_index = getGeneratorIndex(clicked);
                                if (remove_index != null) {
                                    this.global_spawners.remove(remove_index);
                                    Messenger.Send(player, "Spawner removed from game!", MessageType.EDIT, PrefixType.EDITOR);// Gen. removed message
                                    this.editing = GameEditorTool.INACTIVE;
                                }
                            } else {
                                if (locFree(clicked)) {
                                    switch (this.editing) {
                                        case SPAWN_LOC:
                                            this.spawn_location = clicked.add(0.5,1,0.5);
                                            SendEditorMsg("Game spawn", event.getClickedBlock().getLocation());
                                            if (this.global_spawners.size() < 1) {
                                                Send(this.creator, "You should create some item spawners!", MessageType.INFO);
                                            }
                                            this.editing = GameEditorTool.INACTIVE;
                                            //this.Save();
                                            break;
                                        case DIAMOND_GENERATOR:
                                            global_spawners.add(new DiamondSpawner(event.getClickedBlock().getLocation(), this.main));
                                            this.main.getServer().getPluginManager().registerEvents(this.global_spawners.get(this.global_spawners.size()-1), this.main);
                                            SendEditorMsg("Diamond generator", event.getClickedBlock().getLocation());
                                            //this.Save();
                                            this.editing = GameEditorTool.INACTIVE;
                                            break;
                                        case EMERALD_GENERATOR:
                                            global_spawners.add(new EmeraldSpawner(event.getClickedBlock().getLocation(), this.main));
                                            this.main.getServer().getPluginManager().registerEvents(this.global_spawners.get(this.global_spawners.size()-1), this.main);
                                            SendEditorMsg("Emerald generator", event.getClickedBlock().getLocation());
                                            //this.Save();
                                            this.editing = GameEditorTool.INACTIVE;
                                            break;
                                    }
                                }
                            }
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void MapProtector(BlockBreakEvent event) {
        if (this.state == GameState.IN_PROGRESS) {
            boolean nexus = false;
            for (GameTeam t: this.teams) {
                if (event.getBlock().getLocation() == t.nexus.getLoc() && !t.Contains(event.getPlayer())) {
                    nexus = true;
                }
            }
            if (this.map.getBlockAt(event.getBlock().getLocation()).getType() != Material.AIR && !nexus) {
                Messenger.Send(event.getPlayer(), "Sorry, but you can't edit map!", MessageType.ERROR, PrefixType.NONE);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void ExplosionProtector(EntityExplodeEvent event) {
        if (this.state == GameState.IN_PROGRESS) {
            ArrayList<Block> protect = new ArrayList<>();
            for (int i = 0; i < event.blockList().size(); i++) {
                if (this.map.getBlockAt(event.blockList().get(i).getLocation()).getType() != Material.AIR) {
                    protect.add(event.blockList().get(i));
                }
            }
            event.blockList().removeAll(protect);
        }
    }

    @EventHandler
    public void BurnProtector(BlockBurnEvent event) {
        if (this.state == GameState.IN_PROGRESS) {
            if (this.map.getBlockAt(event.getBlock().getLocation()).getType() != Material.AIR) {
                event.setCancelled(true);
            }
        }
    }
}
