package com.kamilereon.npccontroller.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
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

    public static List<Location> getLocationsByPredicate(Location loc, int radius, Predicate<Location> predicate) {
        List<Location> list = new ArrayList<>();
        for(double x = loc.getX() - radius; x <= loc.getX() + radius; x++) {
            for (double y = loc.getY() - radius; y <= loc.getY() + radius; y++) {
                for (double z = loc.getZ() - radius; z <= loc.getZ() + radius; z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if(predicate.test(l)) list.add(l);
                }
            }
        }
        return list;
    }

    public static List<Entity> getEntitiesByPredicate(Location loc, double radius, Predicate<Entity> predicate) {
        List<Entity> list = new ArrayList<>();
        for(Entity e : loc.getWorld().getEntities()) {
            if(predicate.test(e)) {
                if(e.getLocation().distance(loc) < radius) list.add(e);
            }
        }
        return list;
    }
}
