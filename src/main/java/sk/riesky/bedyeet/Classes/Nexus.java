package sk.riesky.bedyeet.Classes;

import javafx.util.Pair;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import sk.riesky.bedyeet.Events.GameStartEvent;
import sk.riesky.bedyeet.GamePlayer;
import sk.riesky.bedyeet.GameTeam;
import sk.riesky.bedyeet.Resources.Constants;

import java.util.List;
import java.util.UUID;


public class Nexus implements Listener {
    Location loc;
    Integer damage = 0;
    List<Pair> hardness;
    private final GameTeam parent;

    public Nexus(Location l, GameTeam _parent) {
        this.loc = l;
        this.parent = _parent;
        this.hardness = Constants.getNexusHardness(this.parent.getName());
        this.loc.getBlock().setType((Material) hardness.get(this.hardness.size()-1).getValue());
    }

    public void Destroy() {
        if (this.loc != null) {
            this.loc.getBlock().setType(Material.AIR);
        }
    }

    public Location getLoc() {
        return this.loc;
    }

    public void takeDamage() {
        if (this.damage < Constants.HEALTH) {
            this.loc.getWorld().playSound(this.loc, Sound.BLOCK_ANVIL_FALL, 3.0f, 0.5f);
            damage++;
            if (this.parent.getPlayers().size() > 0) {
                for (GamePlayer p : this.parent.getPlayers()) {
                    p.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(String.valueOf(Constants.HEALTH - this.damage)).color(ChatColor.RED).create());
                }
            }
            Material fianl_mat =  this.loc.getBlock().getType();
            for (int i = 0; i < this.hardness.size(); i++) {
                if (Constants.HEALTH - this.damage <= (Integer) this.hardness.get(i).getKey()) {
                    fianl_mat = (Material) this.hardness.get(i).getValue();
                    break;
                }
            }
            this.loc.getBlock().setType(fianl_mat);
        } else {
            this.Destroy();
        }
    }

    @EventHandler
    public void OnGameStart(GameStartEvent event) {
        this.damage = 0;
        this.loc.getBlock().setType((Material) hardness.get(this.hardness.size()-1).getValue());
    }

    @EventHandler
    public void OnBreak(BlockBreakEvent event) {
        if(this.loc.equals(event.getBlock().getLocation())) {
            takeDamage();
            event.setCancelled(true);
        }
    }

}
