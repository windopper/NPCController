package com.kamilereon.npccontroller.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Predicate;

public class WorldUtils {
    public static Entity getNearest(Location location, Predicate<Entity> predicate, double radius, Class<? extends Entity> clazz) {
        double dist = radius;
        Entity nearest = null;
        for(Entity t : location.getWorld().getEntitiesByClass(clazz)) {
            if(!predicate.test(t)) continue;
            Location loc = t.getLocation();
            if(loc.distance(location) < dist) {
                dist = loc.distance(location);
                nearest = t;
            }
        }
        return nearest;
    }
}
