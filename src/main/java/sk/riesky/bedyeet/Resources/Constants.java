package sk.riesky.bedyeet.Resources;

import org.bukkit.ChatColor;
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

    // Commands
    public static final String main_command = "yyt";
}