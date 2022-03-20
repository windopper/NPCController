package trader.behaviors;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryImportance;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import com.kamilereon.npccontroller.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;

public class FindTraders extends Behavior {

    public FindTraders(NPCManager npcManager) {
        super(npcManager);
    }

    @Override
    public boolean check() {

        if(npcManager.getRandom().nextDouble() > 0.5) return false;
        if(this.npcManager.getMemoryModuleIfPresent("mytrader") != null) return false;

        Location loc = npcManager.getLocation();
        List<Entity> entities = WorldUtils.getEntitiesByPredicate(loc, 20, (e) -> {
            for(NPCManager npcManager : NPCController.getNpcManagers()) {
                if(npcManager.getMemoryModuleIfPresent("mytrader") != null) return false;
                if(npcManager.getMemoryModuleIfPresent("Trader") == null) return false;
                if(npcManager.getAI().getId() == e.getEntityId() && e.getEntityId() != this.npcManager.getAI().getId()) return true;
            }
            return false;
        });

        if(entities.size()==0) return false;
        Entity entity = entities.get(npcManager.getRandom().nextInt(entities.size()));
        NPCManager targetNPCManager = null;
        for(NPCManager npcManager : NPCController.getNpcManagers()) {
            if(npcManager.getAI().getId() == entity.getEntityId()) targetNPCManager = npcManager;
        }
        if(targetNPCManager == null) return false;
        npcManager.getBehaviorContainer().createDestinationMemory(entity, MemoryImportance.HIGH);
        npcManager.putMemoryModule(new MemoryModule<>(MemoryType.SHORT_TERM, "mytrader", targetNPCManager));
        return true;
    }

    @Override
    public void act() {

    }
}
