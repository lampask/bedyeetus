package sk.riesky.bedyeet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import sk.riesky.bedyeet.Classes.DiamondSpawner;
import sk.riesky.bedyeet.Classes.EmeraldSpawner;
import sk.riesky.bedyeet.Classes.Spawner;
import sk.riesky.bedyeet.Manager.GameEditorTool;
import sk.riesky.bedyeet.Manager.GameState;
import sk.riesky.bedyeet.Resources.Constants;
import sk.riesky.bedyeet.Resources.Messenger;

import java.util.ArrayList;
import java.util.Objects;

import static sk.riesky.bedyeet.Resources.Messenger.*;

public class Game implements Listener {

    private final Bedyeetus main;
    String name;
    private Player creator;
    private ArrayList<GameTeam> teams = new ArrayList<>();
    GameState state = GameState.EDITING;
    private GameEditorTool editing;
    private Location spawn_location;
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
    }
    public String getDisplay_name() {
        return Constants.STATE_COLOR.get(state)+name+ChatColor.RESET;
    }

    public void addTeam(GameTeam t) {
        this.teams.add(t);
    }

    public void removeTeam(GameTeam t) {
        this.teams.remove(t);
    }

    public GameTeam getTeam(String name) {
        for (GameTeam t: this.teams) {
            if (t.name == name) {
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
                                }
                            } else {
                                if (locFree(clicked)) {
                                    switch (this.editing) {
                                        case SPAWN_LOC:
                                            this.spawn_location = clicked;
                                            SendEditorMsg("Game spawn", event.getClickedBlock().getLocation());
                                            if (this.global_spawners.size() < 1) {
                                                Send(this.creator, "You should create some item spawners!", MessageType.INFO);
                                            }
                                            this.editing = GameEditorTool.INACTIVE;
                                            break;
                                        case DIAMOND_GENERATOR:
                                            global_spawners.add(new DiamondSpawner(event.getClickedBlock().getLocation()));
                                            SendEditorMsg("Diamond generator", event.getClickedBlock().getLocation());
                                            break;
                                        case EMERALD_GENERATOR:
                                            global_spawners.add(new EmeraldSpawner(event.getClickedBlock().getLocation()));
                                            SendEditorMsg("Emerald generator", event.getClickedBlock().getLocation());
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
            if (event.getBlock().equals(this.map.getBlockAt(event.getBlock().getLocation()))) {
                Messenger.Send(this.creator, "Sorry, but you can't edit map!", MessageType.ERROR, PrefixType.NONE);
                event.setCancelled(true);
            }
        }
    }
}
