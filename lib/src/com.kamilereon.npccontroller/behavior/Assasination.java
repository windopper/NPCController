package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.utils.WorldUtils;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.pathfinder.PathEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

public class Assasination extends Behavior{

    private double radius;
    private Entity target = null;

    public Assasination(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        Location location = npcManager.getAI().getBukkitEntity().getLocation();
        double dist = radius;
        for(Entity entity : location.getWorld().getEntitiesByClass(Skeleton.class)) {
            if(entity.getWorld() != location.getWorld()) continue;
            Location ploc = entity.getLocation();
            if(ploc.distance(location) < dist) {
                dist = ploc.distance(location);
                target = entity;
            }
        }

        return target != null;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        if(target == null) return false;
        if(npcManager.getLocation().distance(target.getLocation()) > radius) {
            return false;
        }
        return true;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.setEquipment(ItemSlot.MAIN_HAND, new ItemStack(Material.NETHERITE_SWORD));
    }

    @Override
    public void endAct(NPCManager npcManager) {
        npcManager.setEquipment(ItemSlot.MAIN_HAND, new ItemStack(Material.AIR));
        target = null;
    }

    @Override
    public void act(NPCManager npcManager) {
        EntityZombie ev = npcManager.getAI();
        Location loc = target.getLocation();
        ev.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 1.6);
        double dist = ev.getBukkitEntity().getLocation().distance(loc);
        if(dist < 10) {
            ev.getControllerLook().a(loc.getX(), loc.getY(), loc.getZ());
        }
        if(dist < 3.5) {
            npcManager.attack(target);
            target = null;
        }
    }
}
