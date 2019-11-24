package sk.riesky.bedyeet;

import org.apache.commons.collections4.EnumerationUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.lang.reflect.Field;

public class GamePlayer {
    public Player player;
    private GameTeam parent;

    public ArmorTypes armor = ArmorTypes.LEATHER;
    public SwordTypes sword = SwordTypes.WOODEN;
    public boolean hasShears = false;

    enum ArmorTypes {
        LEATHER,
        CHAINMAIL,
        IRON,
        DIAMOND
    };
    enum  SwordTypes {
        WOODEN,
        STONE,
        IRON,
        DIAMOND
    }

    public GamePlayer(Player p, GameTeam parent) {
        this.player = p;
        this.parent = parent;
    }

    public void give_armor(ArmorTypes type) {
        ItemStack boots = null;
        ItemStack leggings = null;
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        try {
            Field boots_field = Class.forName("org.bukkit.Material").getField(type+"_BOOTS");
            boots = new ItemStack((Material)boots_field.get(null));
            Field leggings_field = Class.forName("org.bukkit.Material").getField(type+"_LEGGINGS");
            leggings = new ItemStack((Material)leggings_field.get(null));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (boots.getType() == Material.LEATHER_BOOTS && leggings.getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta boots_meta = (LeatherArmorMeta) boots.getItemMeta();
            LeatherArmorMeta leggings_meta = (LeatherArmorMeta) leggings.getItemMeta();
            boots_meta.setColor(parent.getDye());
            leggings_meta.setColor(parent.getDye());
            boots.setItemMeta(boots_meta);
            leggings.setItemMeta(leggings_meta);
        }
        LeatherArmorMeta chestplate_meta = (LeatherArmorMeta) chestplate.getItemMeta();
        LeatherArmorMeta helmet_meta = (LeatherArmorMeta) helmet.getItemMeta();
        chestplate_meta.setColor(parent.getDye());
        helmet_meta.setColor(parent.getDye());
        chestplate.setItemMeta(chestplate_meta);
        helmet.setItemMeta(helmet_meta);
        player.getInventory().setArmorContents(new ItemStack[] {
                this.addUnbreakable(boots),
                this.addUnbreakable(leggings),
                this.addUnbreakable(chestplate),
                this.addUnbreakable(helmet)
        });
    }

    public void give_sword(SwordTypes type) {
        ItemStack sword = null;
        try {
            Field sword_field = Class.forName("org.bukkit.Material").getField(type+"_SWORD");
            sword = new ItemStack((Material)sword_field.get(null));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (sword != null) {
            ItemMeta sword_meta = sword.getItemMeta();
            sword_meta.setUnbreakable(true);
            sword.setItemMeta(sword_meta);
        }
        player.getInventory().addItem(sword);
    }

    public void give_itemStack(ItemStack s) {

        player.getInventory().addItem(s);
    }

    private ItemStack addUnbreakable(ItemStack target) {
        ItemMeta meta = target.getItemMeta();
        meta.setUnbreakable(true);
        target.setItemMeta(meta);
        return target;
    }

    public void giveDefaultItems() {
        this.give_armor(this.armor);
        this.give_sword(this.sword);
        if (this.hasShears == true) {
            this.give_itemStack(addUnbreakable(new ItemStack(Material.SHEARS)));
        }
    }

}
