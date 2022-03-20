package archer;

import archer.behaviors.BowShoot;
import archer.behaviors.SelectTarget;
import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.standard.Idle;
import com.kamilereon.npccontroller.behavior.standard.RandomLookAround;
import com.kamilereon.npccontroller.states.ItemSlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Archer {
    public static void create(Location location) {
        NPCManager npcManager = NPCController.createNewNPC(location, "archer", "", "");
        npcManager.setAI();
        npcManager.showToAll();
        npcManager.setEquipment(ItemSlot.MAIN_HAND, new ItemStack(Material.BOW, 1));

        npcManager.setBehavior(0, new SelectTarget(npcManager));
        npcManager.setBehavior(1, new BowShoot(npcManager));
        npcManager.setBehavior(10, new Idle(npcManager, 1.2, 0.75));
        npcManager.setBehavior(11, new RandomLookAround(npcManager));
    }
}
