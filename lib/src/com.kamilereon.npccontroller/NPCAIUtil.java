package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface NPCAIUtil {

    MemoryModule<?> getMemoryModuleIfPresent(String key);

    void putMemoryModule(MemoryModule<?> memoryModule);

    void removeMemoryModule(String key);

    void setBehavior(int priority, Behavior behavior);

    void setBehavior(int priority, Behavior behavior, boolean forceStart);

    boolean navigateTo(Location location, double speed, int nearbyDist);

    void jump();

    void attack(Entity target);
}
