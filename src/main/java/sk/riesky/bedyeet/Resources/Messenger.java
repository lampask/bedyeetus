package sk.riesky.bedyeet.Resources;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messenger {

    public enum PrefixType {
        NONE,
        DEFAULT,
        EDITOR
    }

    public enum MessageType {
        INFO,
        EDIT,
        WARNING,
        ERROR,
        ANNOUNCE
    }

    public static void Send(CommandSender receiver, String message) {
        receiver.sendMessage(String.format("%s[%s] %s %s", ChatColor.DARK_GRAY, Constants.PREFIX_TYPE.get(PrefixType.DEFAULT)+ChatColor.DARK_GRAY, Constants.MESSAGE_TYPE.get(MessageType.INFO), message));
    }

    public static void Send(CommandSender receiver, String message, MessageType type) {
        receiver.sendMessage(String.format("%s[%s] %s %s", ChatColor.DARK_GRAY, Constants.PREFIX_TYPE.get(PrefixType.DEFAULT)+ChatColor.DARK_GRAY, Constants.MESSAGE_TYPE.get(type), message));
    }

    public static void Send(CommandSender receiver, String message, Object... specifications) {
        MessageType type = null;
        PrefixType prefix = null;
        if (specifications[0] instanceof MessageType && specifications[1] instanceof PrefixType) {
            type = (MessageType) specifications[0];
            prefix = (PrefixType) specifications[1];
        } else if (specifications[0] instanceof PrefixType && specifications[1] instanceof MessageType) {
            type = (MessageType) specifications[1];
            prefix = (PrefixType) specifications[0];
        }
        if (specifications[1] == PrefixType.NONE) {
            receiver.sendMessage(String.format("%s %s", Constants.MESSAGE_TYPE.get(type), message));
        } else {
            receiver.sendMessage(String.format("%s[%s] %s %s", ChatColor.DARK_GRAY, Constants.PREFIX_TYPE.get(prefix) + ChatColor.DARK_GRAY, Constants.MESSAGE_TYPE.get(type), message));
        }
    }
}
