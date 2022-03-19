package fisherman.behaviors;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProtectingMyFishes extends Behavior {

    protected Player target;
    protected Location loc;
    protected double radius;

    public ProtectingMyFishes(double radius) {
        this.forceStartIfNecessary = true;
        this.canForceStop = false;
        this.radius = radius;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        MemoryModule<?> fishchest = npcManager.getMemoryModuleIfPresent("fishchest");
        if(fishchest != null) {
            this.loc = (Location) fishchest.getData();
            if(loc.getBlock().getType() == Material.CHEST) {
                Chest chest = (Chest) loc.getBlock().getState();
                if(chest.getBlockInventory().getViewers().size() >= 1) {
                    this.target = (Player) chest.getBlockInventory().getViewers().get(0);
                }
            }
        }
        return this.target != null;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return target.getLocation().distance(loc) < radius;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.forgetMemory(MemoryType.WORKING);
        npcManager.setEquipment(ItemSlot.MAIN_HAND, null);
        npcManager.navigateTo(target.getLocation(), 1.5, 3);
    }

    @Override
    public void endAct(NPCManager npcManager) {
        this.loc = null;
        this.target = null;
    }

    @Override
    public void act(NPCManager npcManager) {

        boolean value = npcManager.navigateTo(target.getLocation(), 1.5, 3);
        if(npcManager.distanceTo(target.getLocation())<3.5) {
            npcManager.lookAt(target.getEyeLocation());
            npcManager.playAnimation(Animation.SWING_MAIN_ARM);
            npcManager.attack(target);
            target.closeInventory();
        }
    }
}
