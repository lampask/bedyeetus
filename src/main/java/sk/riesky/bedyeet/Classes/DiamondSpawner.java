package sk.riesky.bedyeet.Classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import sk.riesky.bedyeet.Bedyeetus;
import sk.riesky.bedyeet.Events.GameStartEvent;

public class DiamondSpawner extends Spawner {
    private long delay = 120000/50;
    private int iterator = 0;

    public DiamondSpawner(Location loc, Bedyeetus main) {
        super(loc, main, Material.DIAMOND_BLOCK, new ItemStack(Material.DIAMOND));
    }

    void runTask() {
        cancelTask();
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {

            @Override
            public void run() {
                iterator++;
                Bukkit.getLogger().info("created");
                if (iterator == 10) {
                    delay = 80000/50;
                } else if (iterator == 40) {
                    delay = 60000/50;
                }
                spawnItem();
                task = main.getServer().getScheduler().runTaskLater(main, this, delay);
            }

        }, this.delay);
    }

    void cancelTask() {
        if (task != null) try {
            task.cancel();
        } catch(Throwable ex) {
            // Ignore.
        }
    }

    @EventHandler
    public void OnGameStart(GameStartEvent event) {
        this.runTask();
    }
}
