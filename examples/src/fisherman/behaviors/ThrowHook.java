package fisherman.behaviors;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fish;
import org.bukkit.entity.FishHook;
import org.bukkit.inventory.ItemStack;

public class ThrowHook extends Behavior {

    protected Location fishingPoint;
    protected int tick;

    public ThrowHook() {

    }

    @Override
    public boolean check(NPCManager npcManager) {
        if(fishingPoint == null) fishingPoint = new Location(npcManager.getLocation().getWorld(), 166, 64, 47);
        if(npcManager.getMemoryModuleIfPresent("throw_fishhook") != null) {
            MemoryModule<?> throw_fishhook = npcManager.getMemoryModuleIfPresent("throw_fishhook");
            FishHook fishHook = (FishHook) throw_fishhook.getData();
            if(fishHook.getHookedEntity() != null || fishHook.isOnGround() || fishHook.isDead()) {
                fishHook.remove();
                npcManager.removeMemoryModule("throw_fishhook");
            }
            return false;
        }
        if(npcManager.getUsedSlotInInventory() < 5) return true;
        else {
            return false;
        }
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        Location loc = npcManager.getLocation();
        return loc.distance(fishingPoint) > 2 || this.tick > 0;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.navigateTo(fishingPoint, 1.5, 1);
        npcManager.setEquipment(ItemSlot.MAIN_HAND, new ItemStack(Material.FISHING_ROD, 1));
        this.tick = 30;
    }

    @Override
    public void endAct(NPCManager npcManager) {
        EntityPlayer npc = npcManager.getNPC();
        FishHook fishHook = npc.getBukkitEntity().launchProjectile(FishHook.class);
        npcManager.playAnimation(Animation.SWING_MAIN_ARM);
        npcManager.putMemoryModule(new MemoryModule<>(MemoryType.WORKING, "throw_fishhook", fishHook));
    }

    @Override
    public void act(NPCManager npcManager) {
        --this.tick;
        npcManager.navigateTo(fishingPoint, 1.5, 1);
        npcManager.lookAt(fishingPoint.clone().add(0, 2, 0));
    }
}
