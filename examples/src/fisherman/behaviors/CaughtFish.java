package fisherman.behaviors;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import net.minecraft.world.entity.projectile.EntityFishingHook;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftFishHook;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.FishHook;

public class CaughtFish extends Behavior {

    protected FishHook fishHook;
    protected int tick;

    public CaughtFish() {
        this.forceStartIfNecessary = true;
        this.canForceStop = false;
    }

    @Override
    public boolean check(NPCManager npcManager) {

        MemoryModule<?> throw_fishhook = npcManager.getMemoryModuleIfPresent("throw_fishhook");
        MemoryModule<?> caught_fish = npcManager.getMemoryModuleIfPresent("caught_fish");

        if(caught_fish == null) return false;

        if(throw_fishhook == null) return false;

        FishHook f = (FishHook) throw_fishhook.getData();
        if(f == null) return false;

        this.fishHook = f;

        return true;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.lookAt(fishHook.getLocation());
        this.tick = 20;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return this.tick > 0;
    }

    @Override
    public void endAct(NPCManager npcManager) {
        EntityFishingHook entityFishingHook = ((CraftFishHook) this.fishHook).getHandle();
        entityFishingHook.a(CraftItemStack.asNMSCopy(npcManager.getNPC().getBukkitEntity().getEquipment().getItemInMainHand()));
        npcManager.removeMemoryModule("throw_fishhook");
        npcManager.removeMemoryModule("caught_fish");
        this.fishHook = null;
    }

    @Override
    public void act(NPCManager npcManager) {
        --this.tick;
    }
}
