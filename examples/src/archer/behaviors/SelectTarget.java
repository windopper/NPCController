package archer.behaviors;

import com.kamilereon.npccontroller.AIEntity;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import com.kamilereon.npccontroller.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class SelectTarget extends Behavior {

    protected Entity target;

    public SelectTarget(NPCManager npcManager) {
        super(npcManager);
        this.forceStartIfNecessary = true;
    }

    @Override
    public boolean check() {
        if(npcManager.getMemoryModuleIfPresent("target") != null) {
            MemoryModule<?> module = npcManager.getMemoryModuleIfPresent("target");
            Entity t = (Entity) module.getData();
            if(npcManager.distanceTo(t.getLocation()) >= 20) {
                npcManager.removeMemoryModule("target");
            }
            return false;
        }

        List<Entity> list = WorldUtils.getEntitiesByPredicate(npcManager.getLocation(), 10, (e) -> {
            if(e == npcManager.getAI()) return false;
            return true;
        });
        this.target = npcManager.getRandomOne(list);
        if(this.target != null) {
            npcManager.putMemoryModule(new MemoryModule<>(MemoryType.WORKING, "target", this.target));
            return true;
        }
        return false;
    }

    @Override
    public boolean whileCheck() {
        return false;
    }

    @Override
    public void act() {

    }
}
