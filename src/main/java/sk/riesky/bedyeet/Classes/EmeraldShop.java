package sk.riesky.bedyeet.Classes;

import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class EmeraldShop extends Shop implements Listener {
    String name = ChatColor.DARK_GREEN + "Emerald Shop";

    public EmeraldShop(Location loc) {
        super(loc);
        this.shop_villager.setCustomName(this.name);
        this.shop_gui = Bukkit.createInventory(this, 45, this.name);
        super.prices = Arrays.asList(
                new Pair<>(19, 4),
                new Pair<>(20, 6),
                new Pair<>(21, 4),
                new Pair<>(22, 1),
                new Pair<>(23, 1),
                new Pair<>(24, 1),
                new Pair<>(25, 4),
                new Pair<>(31, 1)
        );
        this.initializeItems();
    }

    private String getPrice(Integer index) {
        return ChatColor.DARK_GREEN+super.prices.get(index).getValue().toString()+" Emeralds";
    }

    public void initializeItems() {
        super.getInventory().setItem(super.prices.get(0).getKey(), super.createGuiItem(Material.OBSIDIAN, ChatColor.DARK_PURPLE+"Obsidian", this.getPrice(0), "4 Blocks of obsidian"));
        super.getInventory().setItem(super.prices.get(1).getKey(), super.createGuiItem(Material.DIAMOND_CHESTPLATE, ChatColor.AQUA+"Diamond Armor", this.getPrice(1), "Boots and leggings of diamond armor"));
        super.getInventory().setItem(super.prices.get(2).getKey(), super.createGuiItem(Material.DIAMOND_SWORD, ChatColor.AQUA+"Diamond Sword", this.getPrice(2), "lol"));
        super.getInventory().setItem(super.prices.get(3).getKey(), super.createGuiItem(Material.POTION, ChatColor.DARK_AQUA+"Potion of Swiftness II", this.getPrice(3), "lol"));
        super.getInventory().setItem(super.prices.get(4).getKey(), super.createGuiItem(Material.POTION, ChatColor.GREEN+"Potion of Leaping V", this.getPrice(4), "lol"));
        super.getInventory().setItem(super.prices.get(5).getKey(), super.createGuiItem(Material.POTION, ChatColor.GRAY+"Potion of Invisibility", this.getPrice(5), "lol"));
        super.getInventory().setItem(super.prices.get(6).getKey(), super.createGuiItem(Material.ENDER_PEARL, ChatColor.LIGHT_PURPLE+"Ender Pearl", this.getPrice(6), "lol"));
        super.getInventory().setItem(super.prices.get(7).getKey(), super.createGuiItem(Material.WATER_BUCKET, ChatColor.DARK_BLUE+"Water Bucket", this.getPrice(7), "lol"));
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() != this) {
            return;
        }
        if (e.getClick().equals(ClickType.NUMBER_KEY)){
            e.setCancelled(true);
        }
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        ItemStack swift = new ItemStack(Material.POTION, 1);
        PotionMeta swift_meta = (PotionMeta) swift.getItemMeta();
        swift_meta.setBasePotionData(new PotionData(PotionType.SPEED, false, true));
        ItemStack leap = new ItemStack(Material.POTION, 1);
        PotionMeta leap_meta = (PotionMeta) leap.getItemMeta();
        leap_meta.setBasePotionData(new PotionData(PotionType.JUMP, false, true));
        ItemStack invis = new ItemStack(Material.POTION, 1);
        PotionMeta invis_meta = (PotionMeta) invis.getItemMeta();
        invis_meta.setBasePotionData(new PotionData(PotionType.INVISIBILITY, true, false));

        List<ItemStack[]> actions = Arrays.asList(
                new ItemStack[] { new ItemStack(Material.OBSIDIAN, 4) },
                new ItemStack[] { new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_LEGGINGS) },
                new ItemStack[] { new ItemStack(Material.DIAMOND_SWORD, 1) },
                new ItemStack[] { swift },
                new ItemStack[] { leap },
                new ItemStack[] { invis },
                new ItemStack[] { new ItemStack(Material.ENDER_PEARL, 1) },
                new ItemStack[] { new ItemStack(Material.WATER_BUCKET, 1) }
        );

        for (int i = 0; i < actions.size(); i++) {
            if (e.getRawSlot() == super.prices.get(i).getKey()) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), super.prices.get(i).getValue())) {
                    p.getInventory().removeItem(new ItemStack(Material.EMERALD, super.prices.get(i).getValue()));
                    if (i != 1) {
                        p.getInventory().addItem(actions.get(i));
                    } else {
                        p.getInventory().setArmorContents(actions.get(i));
                    }
                } else {
                    p.sendMessage(ChatColor.RED+"You don't have enough emeralds.");
                }
            }
        }

    }
}
