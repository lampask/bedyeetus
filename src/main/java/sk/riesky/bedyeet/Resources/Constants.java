package sk.riesky.bedyeet.Resources;

import javafx.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static sk.riesky.bedyeet.Resources.Messenger.MessageType;
import static sk.riesky.bedyeet.Resources.Messenger.PrefixType;
import static sk.riesky.bedyeet.Manager.GameState;

public class Constants {

    public static final Integer HEALTH = 100;
    public static final String CHARS = "\u26A0 \u274C";
    public static final long DELAY = 200000;

    public static final Map<MessageType, String> MESSAGE_TYPE = Map.ofEntries(
            Map.entry(MessageType.INFO, ChatColor.WHITE.toString()),
            Map.entry(MessageType.EDIT, ChatColor.GREEN.toString()),
            Map.entry(MessageType.ANNOUNCE, ChatColor.YELLOW.toString()),
            Map.entry(MessageType.WARNING, ChatColor.GOLD+"\u26A0"),
            Map.entry(MessageType.ERROR, ChatColor.RED+"\u274C")
    );
    public static final Map<PrefixType, String> PREFIX_TYPE = Map.ofEntries(
            Map.entry(PrefixType.DEFAULT, ChatColor.WHITE+"Yeet"),
            Map.entry(PrefixType.EDITOR, ChatColor.AQUA+"Editor")
    );
    public static final Map<GameState, ChatColor> STATE_COLOR = Map.ofEntries(
            Map.entry(GameState.EDITING, ChatColor.AQUA),
            Map.entry(GameState.PREPARED, ChatColor.YELLOW),
            Map.entry(GameState.WAITING, ChatColor.GREEN),
            Map.entry(GameState.IN_PROGRESS, ChatColor.LIGHT_PURPLE)
    );

    public static List<Pair> getNexusHardness(String color) {
        Material concrete;
        Material concrete_powder;
        Material glass;
        Material terracota;
        try {
            Field concrete_field = Class.forName("org.bukkit.Material").getField(color.toUpperCase()+"_CONCRETE");
            concrete = (Material) concrete_field.get(null);
            Field concrete_powder_field = Class.forName("org.bukkit.Material").getField(color.toUpperCase()+"_CONCRETE_POWDER");
            concrete_powder = (Material) concrete_powder_field.get(null);
            Field glass_field = Class.forName("org.bukkit.Material").getField(color.toUpperCase()+"_STAINED_GLASS");
            glass = (Material) glass_field.get(null);
            Field terracota_field = Class.forName("org.bukkit.Material").getField(color.toUpperCase()+"_TERRACOTTA");
            terracota = (Material) terracota_field.get(null);
        } catch (Exception e) {
            concrete = Material.WHITE_CONCRETE;
            concrete_powder = Material.WHITE_CONCRETE_POWDER;
            glass = Material.WHITE_STAINED_GLASS;
            terracota = Material.WHITE_TERRACOTTA;
    }
        return Arrays.asList(
                new Pair<>(5, Material.OBSIDIAN),
                new Pair<>(15, concrete),
                new Pair<>(30, terracota),
                new Pair<>(50, concrete_powder),
                new Pair<>(100, glass)
        );
    }


    // Commands
    public static final String main_command = "yyt";
}
