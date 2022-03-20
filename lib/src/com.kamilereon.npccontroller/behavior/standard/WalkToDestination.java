package com.kamilereon.npccontroller.behavior.standard;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryImportance;
import com.kamilereon.npccontroller.memory.MemoryModule;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class WalkToDestination extends Behavior {

    static private final String MEMORYNAME = "WalkToDestination";
    protected Object target;
    protected int minDistToTarget;
    protected double speed;

    public WalkToDestination(NPCManager npcManager, int minDistToTarget) {
        super(npcManager);
        this.minDistToTarget = minDistToTarget;
        this.speed = 1.2;
    }

    public WalkToDestination(NPCManager npcManager, int minDistToTarget, double speed) {
        super(npcManager);
        this.minDistToTarget = minDistToTarget;
        this.speed = speed;
    }

    @Override
    public boolean isCanForceStop() {
        MemoryModule<?> destination = npcManager.getMemoryModuleIfPresent(MEMORYNAME);
        if(destination == null) return true;
        if(destination.getImportance() == MemoryImportance.HIGH) return false;
        return true;
    }

    @Override
    public boolean isCanForceStart() {
        MemoryModule<?> destination = npcManager.getMemoryModuleIfPresent(MEMORYNAME);
        if(destination == null) return false;
        if(destination.getImportance() != MemoryImportance.HIGH) return true;
        return false;
    }

    @Override
    public boolean check() {
        MemoryModule<?> destination = npcManager.getMemoryModuleIfPresent(MEMORYNAME);
        if(destination == null) return false;

        if(destination.getData() instanceof Location location) {
            this.target = location;
            return true;
        } else if(destination.getData() instanceof Entity entity) {
            this.target = entity;
            return true;
        }
        return false;
    }

    @Override
    public boolean whileCheck() {
        if(target instanceof Location location) {
            return npcManager.distanceTo(location) >= minDistToTarget + 0.5;
        }
        else if(target instanceof Entity entity ) {
            return npcManager.distanceTo(entity.getLocation()) >= minDistToTarget + 0.5;
        }
        return false;
    }

    @Override
    public void endAct() {
        npcManager.removeMemoryModule(MEMORYNAME);
        this.target = null;

    }

    @Override
    public void act() {
        if(target instanceof Location location) {
            npcManager.navigateTo(location, speed, minDistToTarget);
        }
        else if(target instanceof Entity entity ) {
            npcManager.navigateTo(entity.getLocation(), speed, minDistToTarget);
        }
    }
}
