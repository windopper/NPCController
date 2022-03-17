package com.kamilereon.npccontroller.metadata;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.utils.SchedulerUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BehaviorContainer {

    private final Map<Behavior, Integer> behaviors = new HashMap<>();
    private List<Map.Entry<Behavior, Integer>> sortedBehaviors = new ArrayList<>();
    private final NPCManager npcManager;
    private Behavior currentBehavior;

    public BehaviorContainer(NPCManager npcManager) {
        this.npcManager = npcManager;
    }

    public void setBehavior(int priority, Behavior behavior) {
        behaviors.put(behavior, priority);
        sortBehavior();
    }

    public void behaviorProcess() {

        if(currentBehavior != null) {
            if(currentBehavior.whileCheck(npcManager)) {
                currentBehavior.act(npcManager);
                return;
            } else {
                currentBehavior.endAct(npcManager);
            }
        }

        currentBehavior = null;
        for(Behavior behavior : sortedBehaviors.stream().map(Map.Entry::getKey).collect(Collectors.toSet())) {
            if(behavior.check(npcManager)) {
                currentBehavior = behavior;
                behavior.firstAct(npcManager);
                return;
            }
        }
    }

    private void sortBehavior() {
        sortedBehaviors = new ArrayList<>(behaviors.entrySet());
        sortedBehaviors.sort(Map.Entry.comparingByValue(Integer::compareTo));

    }

    public Map<Behavior, Integer> getBehaviorMap() { return this.behaviors; }


}
