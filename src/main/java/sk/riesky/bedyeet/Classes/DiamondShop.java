package sk.riesky.bedyeet.Classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class DiamondShop extends Shop {
    String name = ChatColor.AQUA + "Diamond Shop";

    public DiamondShop(Location loc) {
        super(loc);
        this.shop_villager.setCustomName(this.name);
        this.shop_gui = Bukkit.createInventory(this, 45, this.name);
    }

    public void initializeItems() {
        super.getInventory().addItem(super.createGuiItem(Material.DIAMOND_SWORD, "Example Sword", "§aFirst line of the lore", "§bSecond line of the lore"));
        super.getInventory().addItem(super.createGuiItem(Material.IRON_HELMET, "§bExample Helmet", "§aFirst line of the lore", "§bSecond line of the lore"));
    }
}
