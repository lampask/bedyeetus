package sk.riesky.bedyeet;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scoreboard.Team;
import sk.riesky.bedyeet.Classes.DiamondShop;
import sk.riesky.bedyeet.Classes.EmeraldShop;
import sk.riesky.bedyeet.Classes.Nexus;
import sk.riesky.bedyeet.Events.GameStartEvent;
import sk.riesky.bedyeet.Manager.TeamEditorTool;
import sk.riesky.bedyeet.Manager.GameEditorTool;
import sk.riesky.bedyeet.Resources.Messenger.*;

import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

import static sk.riesky.bedyeet.Resources.Messenger.Send;

public class GameTeam implements Listener {

    private final Bedyeetus main;
    String name;
    Player creator;
    ChatColor color;
    ArrayList<GamePlayer> players = new ArrayList<>();
    Nexus nexus;
    Location player_spawn;
    TeamEditorTool editing = TeamEditorTool.INACTIVE;
    DiamondShop d_shop;
    EmeraldShop e_shop;
    Game parent;
    Team team;
    Color dye;

    public GameTeam(Bedyeetus _main, Player _creator, Game _patent_g, String _name) {
        this.main = _main;
        this.creator = _creator;
        this.parent = _patent_g;
        this.name = _name;
        try { // try to create color from team name
            Field field = Class.forName("org.bukkit.ChatColor").getField(_name.toUpperCase());
            Field fieldB = Class.forName("org.bukkit.Color").getField(_name.toUpperCase());
            this.color = (ChatColor)field.get(null);
            this.dye = (Color)fieldB.get(null);
        } catch (Exception e) {
            this.color = null; // Not defined
        }
        Send(this.creator, String.format("Team %s was successfully created!", this.getDisplayName()+ChatColor.RESET), MessageType.INFO, PrefixType.EDITOR);
        this.team = this.parent.getBoard().registerNewTeam(this.getName());
        this.team.setCanSeeFriendlyInvisibles(true);
        this.team.setAllowFriendlyFire(false);
        Send(this.creator, "Please select spawn position for this team.", MessageType.INFO, PrefixType.EDITOR);
        this.editing = TeamEditorTool.SPAWN_POINT;
    }

    public void delete() {
        nexus.Destroy();
        e_shop.destroyEntity();
        d_shop.destroyEntity();
    }

    public Color getDye() {
        return dye;
    }

    public String getName() { return this.name; }

    public String getDisplayName() {
        return this.color+this.name;
    }

    public void addPlayer(Player p) {
        this.players.add(new GamePlayer(p, this));
        this.team.addEntry(p.getName());
        p.setScoreboard(this.parent.getBoard());
    }
    public void removePlayer(Player p) {
        this.players.remove(p.getUniqueId());
        this.team.removeEntry(p.getName());
    }

    public boolean Contains(Player p) {
        for (GamePlayer pl: this.players) {
            if (pl.player == p) {
                return true;
            }
        }
        return false;
    }

    public GamePlayer getPlayer(UUID id) {
        for (GamePlayer pl: this.players) {
            if (pl.player.getUniqueId() == id) {
                return pl;
            }
        }
        return null;
    }

    public void setEditorTool (TeamEditorTool tool) {
        this.editing = tool;
    }

    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public ChatColor getColor() {
        return color;
    }

    void checkCompletion() {
        if (this.nexus != null && this.player_spawn != null && this.d_shop != null && this.e_shop != null) {
            Send(this.creator, "Team completed", MessageType.ANNOUNCE);
            this.editing = TeamEditorTool.INACTIVE;
            this.parent.setEditorTool(GameEditorTool.INACTIVE);
        }
    }

    public TeamEditorTool getEditorTool() {
        return this.editing;
    }

    private void SendEditorMsg(String thing, Location l) {
        Send(this.creator, String.format("%s position was set to: x: %s  y: %s  z: %s", thing, l.getX(), l.getY(), l.getZ()), MessageType.EDIT, PrefixType.EDITOR);
    }

    @EventHandler
    public void OnEdit(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.HAND)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                if (player.getInventory().getItemInMainHand().equals(Manager.getWand())) {
                    if (this.getEditorTool() != Manager.TeamEditorTool.INACTIVE) {
                        Location clicked = Objects.requireNonNull(event.getClickedBlock()).getLocation();
                        if (this.getEditorTool() != TeamEditorTool.INACTIVE) {
                            switch (this.getEditorTool()) {
                                case SPAWN_POINT:
                                    this.player_spawn = clicked.add(0.5,1,0.5);
                                    SendEditorMsg("Player spawn", clicked.add(0.5,1,0.5));
                                    if (this.nexus == null) {
                                        Send(this.creator, "Please select nexus position for this team.", MessageType.INFO, PrefixType.EDITOR);
                                        this.editing = TeamEditorTool.NEXUS;
                                    } else {
                                        this.editing = TeamEditorTool.INACTIVE;
                                    }
                                    break;
                                case NEXUS:
                                    if (this.nexus != null) {
                                        this.nexus.Destroy();
                                    }
                                    this.nexus = new Nexus(clicked, this);
                                    this.main.getServer().getPluginManager().registerEvents(this.nexus, main);
                                    SendEditorMsg("Nexus", clicked);
                                    if (this.d_shop == null) {
                                        Send(this.creator, "Please select diamond shop position for this team.", MessageType.INFO, PrefixType.EDITOR);
                                        this.editing = TeamEditorTool.DIAMOND_SHOP;
                                    } else {
                                        this.editing = TeamEditorTool.INACTIVE;
                                    }
                                    break;
                                case DIAMOND_SHOP:
                                    if (this.d_shop != null) {
                                        this.d_shop.destroyEntity();
                                    }
                                    this.d_shop = new DiamondShop(clicked);
                                    this.main.getServer().getPluginManager().registerEvents(this.d_shop, this.main);
                                    SendEditorMsg("Diamond shop", clicked);
                                    if (this.e_shop == null) {
                                        Send(this.creator, "Please select emerald shop position for this team.", MessageType.INFO, PrefixType.EDITOR);
                                        this.editing = TeamEditorTool.EMERALD_SHOP;
                                    } else {
                                        this.editing = TeamEditorTool.INACTIVE;
                                    }
                                    break;
                                case EMERALD_SHOP:
                                    if (this.e_shop != null) {
                                        this.e_shop.destroyEntity();
                                    }
                                    this.e_shop = new EmeraldShop(clicked);
                                    this.main.getServer().getPluginManager().registerEvents(this.e_shop, this.main);
                                    SendEditorMsg("Emerald shop", clicked);
                                    this.editing = TeamEditorTool.INACTIVE;
                                    break;
                            }
                            checkCompletion();
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void OnGameStart(GameStartEvent event) {
        for (GamePlayer player: this.players) {
            player.player.teleport(this.player_spawn);
            player.give_armor(GamePlayer.ArmorTypes.LEATHER);
            player.give_sword(GamePlayer.SwordTypes.WOODEN);
        }
    }

    @EventHandler
    public void OnRespawn(PlayerRespawnEvent event) {
        GamePlayer player = this.getPlayer(event.getPlayer().getUniqueId());
        if (player != null) {
            event.setRespawnLocation(this.player_spawn);
            player.giveDefaultItems();
        }
     }
}
