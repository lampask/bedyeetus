package sk.riesky.bedyeet.Classes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sk.riesky.bedyeet.Bedyeetus;
import sk.riesky.bedyeet.Events.GameStartEvent;
import sk.riesky.bedyeet.Resources.Constants;

public class Spawner implements Listener {
    private Location spawner_location;
    protected ItemStack spawn_target;
    protected Material block;
    BukkitTask task;
    long delay;
    Bedyeetus main;

    public Spawner(Location loc, Bedyeetus main, Material block, ItemStack spawn_target) {
        this.spawner_location = loc;
        this.block = block;
        this.spawn_target = spawn_target;
        this.spawner_location.getBlock().setType(this.block);
        this.main = main;
        this.delay = Constants.DELAY;
    }

    public void spawnItem() {
        this.spawner_location.getWorld().dropItemNaturally(this.spawner_location.add(0.5,1,0.5), this.spawn_target);
    }

    public Location getLocation() {
        return this.spawner_location;
    }
    public Material getBlock() {return this.block; }
}



