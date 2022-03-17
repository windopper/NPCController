package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.behavior.Behavior;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NPCAIUtil {
    void setBehavior(int priority, Behavior behavior);
    void navigateTo(Location location, double speed);
    void jump();
    void attack(Player player);
}
