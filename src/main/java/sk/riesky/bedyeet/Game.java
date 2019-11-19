package sk.riesky.bedyeet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sk.riesky.bedyeet.Manager.GameEditorTool;
import sk.riesky.bedyeet.Manager.GameState;
import sk.riesky.bedyeet.Resources.Constants;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static sk.riesky.bedyeet.Resources.Messenger.*;

public class Game {

    public String name;
    ArrayList<Team> teams;
    GameState state = GameState.EDITING;
    GameEditorTool editing = GameEditorTool.INACTIVE;
    public Integer team_edit_index = null;


    public Game(Player creator, String _name) {
        this.name = _name; // Define the name of game
        //this.state = GameState.EDITING; // Set completion state to (NOT COMPLETED) - editing
        Manager.getInstance().setTo_EditMode(creator.getUniqueId(), this.name); // Mark creator as editor
        Send(creator, String.format("Game %s created in world '%s'", this.name, creator.getWorld().getName()), MessageType.INFO, PrefixType.EDITOR); // Send creation notice
        ItemStack wand = Manager.getWand();
        if (!creator.getInventory().contains(wand)) {
            creator.getInventory().addItem(Manager.getWand()); // Give creator selection item
        }
        // SET SOME GLOBAL PARAMETERS

    }
    public String getDisplay_name() {
        return Constants.STATE_COLOR.get(state)+name+ChatColor.RESET;
    }

    public void addTeam(Team t) {
        this.teams.add(t);
    }

    public void removeTeam(Team t) {
        this.teams.remove(t);
    }

    public Team getTeam(Integer index) {
        return this.teams.get(index);
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
}
