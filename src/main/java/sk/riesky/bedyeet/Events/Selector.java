package sk.riesky.bedyeet.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import sk.riesky.bedyeet.Bedyeetus;
import sk.riesky.bedyeet.Game;
import sk.riesky.bedyeet.Manager;
import sk.riesky.bedyeet.Manager.GameEditorTool;
import sk.riesky.bedyeet.Manager.TeamEditorTool;
import sk.riesky.bedyeet.Team;

public class Selector implements Listener {

    private Bedyeetus main;
    private Manager manager = Manager.getInstance();
    public Selector(Bedyeetus _main) {
        this.main = _main;
    }

    @EventHandler
    public void OnInteract(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.HAND)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                if (player.getInventory().getItemInMainHand().equals(Manager.getWand())) {
                    Game target = manager.getEditedTarget(player.getUniqueId());
                    if (target != null) {
                        if (target.getEditorTool() != GameEditorTool.INACTIVE) {
                            switch (target.getEditorTool()) {
                                case DIAMOND_GENERATOR:
                                    // TBA
                                    break;
                                case EMERALD_GENERATOR:
                                    // TBA
                                    break;
                            }
                        } else if (target.team_edit_index != null) {
                            Team target_team = target.getTeam(target.team_edit_index);
                            if (target_team.getEditorTool() != TeamEditorTool.INACTIVE) {
                                switch (target_team.getEditorTool()) {
                                    case NEXUS:
                                        break;
                                    case SPAWN_POINT:
                                        break;
                                    case DIAMOND_SHOP:
                                        break;
                                    case EMERALD_SHOP:
                                        break;
                                }
                            }
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
