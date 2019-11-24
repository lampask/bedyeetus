package sk.riesky.bedyeet.Classes;

import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;

public class DiamondShop extends Shop {
    String name = ChatColor.AQUA + "Diamond Shop";

    public DiamondShop(Location loc) {
        super(loc);
        this.shop_villager.setCustomName(this.name);
        this.shop_gui = Bukkit.createInventory(this, 45, this.name);
        super.prices = Arrays.asList(
                new Pair<>(19, 4),
                new Pair<>(20, 6),
                new Pair<>(21, 4),
                new Pair<>(22, 1),
                new Pair<>(23, 1),
                new Pair<>(24, 1),
                new Pair<>(25, 4),
                new Pair<>(31, 1)
        );
    }

    public void initializeItems() {
        super.getInventory().addItem(super.createGuiItem(Material.DIAMOND_SWORD, "Example Sword", "§aFirst line of the lore", "§bSecond line of the lore"));
        super.getInventory().addItem(super.createGuiItem(Material.IRON_HELMET, "§bExample Helmet", "§aFirst line of the lore", "§bSecond line of the lore"));
    }


}
