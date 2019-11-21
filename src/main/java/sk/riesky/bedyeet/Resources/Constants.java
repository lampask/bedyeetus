package sk.riesky.bedyeet.Resources;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Map;

import static sk.riesky.bedyeet.Resources.Messenger.MessageType;
import static sk.riesky.bedyeet.Resources.Messenger.PrefixType;
import static sk.riesky.bedyeet.Manager.GameState;

public class Constants {

    public static final String CHARS = "\u26A0 \u274C";

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

    public static final Map<Integer, Material> NEXUS_HARDNESS = Map.ofEntries(
            Map.entry(5, Material.OBSIDIAN),
            Map.entry(15, Material.WHITE_CONCRETE),
            Map.entry(30, Material.TERRACOTTA),
            Map.entry(50, Material.WHITE_CONCRETE_POWDER),
            Map.entry(100, Material.WHITE_STAINED_GLASS)
    );

    // Commands
    public static final String main_command = "yyt";
}
