package sk.riesky.bedyeet.Classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EmeraldSpawner extends Spawner{
    ItemStack spawn_target = new ItemStack(Material.EMERALD);
    Material block = Material.EMERALD_BLOCK;

    public EmeraldSpawner(Location loc) {
        super(loc);
    }
}