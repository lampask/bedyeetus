package sk.riesky.bedyeet.Classes;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Spawner {
    private Location spawner_location;
    protected ItemStack spawn_target;
    protected Material block;

    public Spawner(Location loc) {
        this.spawner_location = loc;
        this.spawner_location.getBlock().setType(block);
    }

    public void spawnItem() {
        this.spawner_location.getWorld().dropItemNaturally(spawner_location.add(0,1,0), spawn_target);
    }

    public Location getLocation() {
        return this.spawner_location;
    }

}



