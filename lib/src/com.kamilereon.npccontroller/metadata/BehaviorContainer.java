package com.kamilereon.npccontroller.metadata;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.behavior.Idle;
import com.kamilereon.npccontroller.behavior.LookAtNearestPlayer;
import com.kamilereon.npccontroller.utils.SchedulerUtils;
import net.minecraft.server.level.EntityPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class BehaviorContainer {

    private final Map<Behavior, Integer> behaviors = new HashMap<>();
    private List<Map.Entry<Behavior, Integer>> sortedBehaviors = new ArrayList<>();
    private final NPCManager npcManager;
    private boolean acting = false;

    public enum EnumBehavior {
        IDLE(new Idle(100, 20)),
        LOOK_AT_NEAREST_PLAYER(new LookAtNearestPlayer(8, p -> true)),
        ;

        private Behavior behavior;

        EnumBehavior(Behavior behavior) {
            this.behavior = behavior;
        }

        public Behavior getBehavior() {
            return behavior;
        }
    }

    public BehaviorContainer(NPCManager npcManager) {
        this.npcManager = npcManager;
    }

    public void setBehavior(int priority, EnumBehavior behavior) {
        setBehavior(priority, behavior.getBehavior());
    }

    public void setBehavior(int priority, Behavior behavior) {
        behaviors.put(behavior, priority);
        sortBehavior();
    }

    public void judgeNextAction() {
        if(acting) return;
        for(Behavior behavior : sortedBehaviors.stream().map(Map.Entry::getKey).collect(Collectors.toSet())) {
            if(behavior.check(npcManager)) {
                acting = true;
                behavior.act(npcManager);
                break;
            }
        }
    }

    private void sortBehavior() {
        sortedBehaviors = new ArrayList<>(behaviors.entrySet());
        sortedBehaviors.sort(Map.Entry.comparingByValue());

    }

    public void disableActing(int tickAfter) {
        SchedulerUtils.runAfter(() -> acting = false, tickAfter);
    }

    public Map<Behavior, Integer> getBehaviorMap() { return this.behaviors; }


}
