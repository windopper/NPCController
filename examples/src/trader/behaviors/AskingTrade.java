package trader.behaviors;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import com.kamilereon.npccontroller.states.ItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class AskingTrade extends Behavior {

    protected NPCManager target;
    protected int tick;

    public AskingTrade(NPCManager npcManager) {
        super(npcManager);
        this.forceStartIfNecessary = true;
        this.canForceStop = false;
    }

    @Override
    public boolean check() {
        MemoryModule<?> mytrader = npcManager.getMemoryModuleIfPresent("mytrader");
        if(mytrader == null) return false;
        if(mytrader.getData() instanceof NPCManager target) {

            if(target.getMemoryModuleIfPresent("mytrader") != null) {
                if(target.getMemoryModuleIfPresent("mytrader").getData() instanceof NPCManager n) {
                    if(n == this.npcManager) {
                        this.target = target;
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }

            if(target.distanceTo(npcManager.getLocation()) < 3) {
                target.putMemoryModule(new MemoryModule<>(MemoryType.SHORT_TERM, "mytrader", this.npcManager));
                this.target = target;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean whileCheck() {
        return npcManager.getMemoryModuleIfPresent("tradedone") == null;
    }

    @Override
    public void endAct() {
        npcManager.setEquipment(ItemSlot.MAIN_HAND, new ItemStack(Material.AIR, 1));
        npcManager.forgetMemory(MemoryType.SHORT_TERM);
        npcManager.spawnParticle(Particle.HEART, npcManager.getLocation().add(0, 2, 0), 10, 0.1, 0.1, 0.1, 0, null);
        this.tick = 0;
        this.target = null;
    }

    @Override
    public void firstAct() {
        this.tick = 40;
    }

    @Override
    public void act() {
        npcManager.lookAt(target.getLocation().add(0, 2, 0));
        --this.tick;
        if(this.tick == 39) {
            npcManager.setEquipment(ItemSlot.MAIN_HAND,
                    new ItemStack(Material.values()[npcManager.getRandom().nextInt(Material.values().length)], 1));
            npcManager.putMemoryModule(new MemoryModule<>(MemoryType.SHORT_TERM, "trade_result", npcManager.getRandom().nextDouble() > 0.9));
        }
        if(this.tick == 0) {
            if(npcManager.getMemoryModuleIfPresent("trade_result") != null && target.getMemoryModuleIfPresent("trade_result") != null) {
                if((boolean) npcManager.getMemoryModuleIfPresent("trade_result").getData() && (boolean) target.getMemoryModuleIfPresent("trade_result").getData()) {
                    npcManager.putMemoryModule(new MemoryModule<>(MemoryType.SHORT_TERM, "tradedone", null));
                }
            }
            this.tick = 40;
        }
    }
}
