package sk.riesky.bedyeet.Classes;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import sk.riesky.bedyeet.GameTeam;
import sk.riesky.bedyeet.Resources.Constants;

import java.util.UUID;


public class Nexus implements Listener {
    Location loc;
    Integer damage;
    private final GameTeam parent;

    public Nexus(Location l, GameTeam _parent) {
        this.loc = l;
        this.parent = _parent;
        this.loc.getBlock().setType(Constants.NEXUS_HARDNESS.get(100));
    }

    public void Destroy() {
        this.loc.getBlock().setType(Material.AIR);
    }

    public void takeDamage() {
        this.loc.getWorld().spawnParticle(Particle.DRIP_LAVA, this.loc, 5);
        this.loc.getWorld().playSound(this.loc, Sound.BLOCK_ANVIL_FALL, 3.0f, 0.5f);
        damage++;
        for (UUID p: this.parent.getPlayers()) {
            Bukkit.getPlayer(p).spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(String.valueOf(100-this.damage)).color(ChatColor.RED).create());;
        }
    }

    @EventHandler
    public void OnBreak(BlockBreakEvent event) {
        if(event.getBlock().getLocation().equals(this.loc)) {
            takeDamage();
            event.setCancelled(true);
        }
    }

}
