package sk.riesky.bedyeet.Classes;

import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class Shop implements InventoryHolder, Listener {

    protected Location shop_location;
    protected Villager shop_villager;
    protected Inventory shop_gui;
    protected List<Pair<Integer, Integer>> prices;

    public Shop(Location loc) {
        this.shop_location = loc.add(0.5,1,0.5);
        this.shop_villager = (Villager) this.shop_location.getWorld().spawnEntity(shop_location, EntityType.VILLAGER);
        this.shop_villager.setAI(false);
        this.shop_villager.setInvulnerable(true);
    }

    public void destroyEntity() {
        if (this.shop_villager != null) {
            this.shop_villager.remove();
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return shop_gui;
    }

    protected ItemStack createGuiItem(Material material, String name, String...lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metalore = new ArrayList<String>();

        for(String lorecomments : lore) {

            metalore.add(lorecomments);

        }

        meta.setLore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    // You can open the inventory with this
    public void openInventory(Player p) {
        p.openInventory(shop_gui);
    }

    @EventHandler
    public void onVillagerClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().equals(this.shop_villager)) {
            openInventory(event.getPlayer());
        }
    }
}
