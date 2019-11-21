package sk.riesky.bedyeet.Classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondSpawner extends Spawner {
    ItemStack spawn_target = new ItemStack(Material.DIAMOND);
    Material block = Material.DIAMOND_BLOCK;

    public DiamondSpawner(Location loc) {
        super(loc);
    }
}
