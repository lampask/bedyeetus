package sk.riesky.bedyeet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import sk.riesky.bedyeet.Manager.TeamEditorTool;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public class Team {

    String name;
    ChatColor color;
    ArrayList<UUID> players;
    Location nexus;
    Location spawner;
    TeamEditorTool editing = TeamEditorTool.INACTIVE;
    int nexus_health;

    void Team(String _name) {
        this.name = _name;
        try { // try to create color from team name
            Field field = Class.forName("org.bukkit.ChatColor").getField(_name.toUpperCase());
            this.color = (ChatColor)field.get(null);
        } catch (Exception e) {
            this.color = null; // Not defined
        }
    }

    void addPlayer(Player p) {
        this.players.add(p.getUniqueId());
    }

    void removePlayer(Player p) {
        this.players.remove(p.getUniqueId());
    }

    void setLocation(Location l) {
        this.nexus = l;
    }

    public void setEditorTool (TeamEditorTool tool) {
        this.editing = tool;
    }

    public TeamEditorTool getEditorTool() {
        return this.editing;
    }
}
