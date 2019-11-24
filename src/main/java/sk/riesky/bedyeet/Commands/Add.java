package sk.riesky.bedyeet.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Add implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ItemStack[] quest_items = new ItemStack[] {

        };
        if (args.length == 2) {
            return true;
        }
        return false;
    }
}
