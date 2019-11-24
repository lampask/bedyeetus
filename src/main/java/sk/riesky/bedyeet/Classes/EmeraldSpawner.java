package sk.riesky.bedyeet.Classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import sk.riesky.bedyeet.Bedyeetus;
import sk.riesky.bedyeet.Events.GameStartEvent;

public class EmeraldSpawner extends Spawner {
    private long delay = 120000/50;
    private int iterator = 0;

    public EmeraldSpawner(Location loc, Bedyeetus main) {
        super(loc, main,  Material.EMERALD_BLOCK, new ItemStack(Material.EMERALD));
    }

    void runTask() {
        cancelTask();
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {

            @Override
            public void run() {
                iterator++;
                Bukkit.getLogger().info("created");
                if (iterator == 20) {
                    delay = 80000/50;
                } else if (iterator == 50) {
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