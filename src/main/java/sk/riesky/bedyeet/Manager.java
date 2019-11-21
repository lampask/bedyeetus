package sk.riesky.bedyeet;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

import static sk.riesky.bedyeet.Resources.Messenger.*;

public class Manager {
    //region Singleton
    private static Manager manager;
    public  static Manager getInstance() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }
    //endregion

    private HashMap<String, Game> games = new HashMap<>();
    private BidiMap<UUID, String> edit_mode = new DualHashBidiMap<>();

    public enum GameState { EDITING, PREPARED, WAITING, IN_PROGRESS }
    public enum GameEditorTool { INACTIVE, SPAWN_LOC,  DIAMOND_GENERATOR, EMERALD_GENERATOR, TEAM, DELETE }
    public enum TeamEditorTool { INACTIVE, NEXUS, SPAWN_POINT, EMERALD_SHOP, DIAMOND_SHOP }

    public void addGame(Game g) {
        this.games.put(g.name, g);
    }

    public void removeGame(CommandSender sender, String name) {
        String displayname = this.games.get(name).getDisplay_name();
        this.games.remove(name);
        Send(sender, "Deletion was successful!", MessageType.EDIT);
        if (this.edit_mode.containsValue(name)) {
            UUID id = edit_mode.getKey(name);
            Send(Bukkit.getPlayer(id), String.format("Game - %s - was deleted during edit mode!!!", displayname), MessageType.WARNING, PrefixType.EDITOR);
            this.unsetFrom_EditMode(id);
        }
    }

    public void setTo_EditMode(UUID id, String game_name) {
        // Mark player as editor of game
        this.edit_mode.put(id, game_name);
        Send(Objects.requireNonNull(Bukkit.getPlayer(id)), "\u2BC8 You have entered edit mode!", MessageType.ANNOUNCE, PrefixType.NONE);
    }

    public void unsetFrom_EditMode(UUID id) {
        // Unmark player as editor of game, if the edited game isn't finished delete it
        Game selected = this.games.get(this.edit_mode.get(id));
        this.edit_mode.remove(id);
        if (selected != null) {
            if (selected.state != GameState.PREPARED) {
                this.removeGame(Bukkit.getPlayer(id),selected.name);
            }
        }
        Send(Objects.requireNonNull(Bukkit.getPlayer(id)), "\u2B8C You have exited edit mode!", MessageType.ANNOUNCE, PrefixType.NONE);
    }

    public boolean isEditing(UUID id) {
        // Check if player is current editor of game
        return this.edit_mode.containsKey(id);
    }

    public Game getEditedTarget (UUID id) {
        // Get currently edited game
        return this.games.get(this.edit_mode.get(id));
    }

    public HashMap<String, Game> getGames() {
        return this.games;
    }

    public static ItemStack getWand() {
        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(ChatColor.LIGHT_PURPLE+"Editor Selector");
        ArrayList<String> lore = new ArrayList<>(List.of("Left/Right click blocks to select them."));
        meta.setLore(lore);
        wand.setItemMeta(meta);
        return wand;
    }
}
