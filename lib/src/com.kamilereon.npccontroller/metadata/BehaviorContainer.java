package com.kamilereon.npccontroller.metadata;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryImportance;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.utils.SchedulerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.stream.Collectors;

public class BehaviorContainer {

    private final Map<Behavior, Integer> behaviors = new HashMap<>();
    private List<Map.Entry<Behavior, Integer>> sortedBehaviors = Collections.synchronizedList(new ArrayList<>());
    private final NPCManager npcManager;
    private Behavior currentBehavior;

    public BehaviorContainer(NPCManager npcManager) {
        this.npcManager = npcManager;
    }

    public void setBehavior(int priority, Behavior behavior) {
        behaviors.put(behavior, priority);
        sortBehavior();
    }

    public Behavior getCurrentBehavior() { return this.currentBehavior; }

    public void removeBehavior(Class<? extends Behavior> behavior) {
        for(Behavior b : behaviors.keySet()) {
            if(b.getClass() == behavior) {
                behaviors.remove(b);
                break;
            }
        }
        sortBehavior();
    }

    public void behaviorProcess() {

        if(currentBehavior != null) {

            // If currentBehavior can forceStop
            if(currentBehavior.isCanForceStop()) {
                for(Behavior behavior : sortedBehaviors.stream().map(Map.Entry::getKey).toList()) {
                    // check forceStart if possible
                    if(!behavior.isCanForceStart()) continue;
                    if(behavior.check()) {
                        currentBehavior.endAct();
                        currentBehavior = behavior;
                        behavior.firstAct();
                        return;
                    }
                }
            }

            if(currentBehavior.whileCheck()) {
                currentBehavior.act();
                return;
            } else {
                currentBehavior.endAct();
                currentBehavior = null;
            }
        }

        for(Behavior behavior : sortedBehaviors.stream().map(Map.Entry::getKey).toList()) {
            if(behavior.check()) {
                currentBehavior = behavior;
                behavior.firstAct();
                return;
            }
        }
    }

    public void forceStopBehavior() {
        currentBehavior.endAct();
        this.currentBehavior = null;
    }

    private void sortBehavior() {
        sortedBehaviors = new ArrayList<>(behaviors.entrySet());
        sortedBehaviors.sort(Map.Entry.comparingByValue(Integer::compareTo));
    }

    public Map<Behavior, Integer> getBehaviorMap() { return this.behaviors; }

    public void createDestinationMemory(Entity target, MemoryImportance importance) {
        this.npcManager.putMemoryModule(new MemoryModule<>(importance, "WalkToDestination", target));
    }

    public void createDestinationMemory(Location target, MemoryImportance importance) {
        this.npcManager.putMemoryModule(new MemoryModule<>(importance, "WalkToDestination", target));
    }

    public void eraseDestinationMemory() {
        this.npcManager.removeMemoryModule("WalkToDestination");
    }


}
