package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.behavior.Behavior;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface NPCAIUtil {
    void setBehavior(int priority, Behavior behavior);
    boolean navigateTo(Location location, double speed, int nearbyDist);
    void jump();
    void attack(Entity target);
}
