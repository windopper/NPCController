package com.kamilereon.npccontroller.v1_17_R1;

import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Villager;

public class MasterEntity_v1_17_R1 extends EntityVillager {
    private MasterEntity_v1_17_R1(World world) {
        super(EntityTypes.aV, world);
    }

    public static Villager summon(Location location) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityVillager entityVillager = new MasterEntity_v1_17_R1(nmsWorld);
//        entityVillager.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        Villager villager = (Villager) entityVillager.getBukkitEntity();
        nmsWorld.addEntity(entityVillager);
        villager.setInvisible(true);
        villager.teleport(location);
        return villager;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return null;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ot;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.os;
    }
}
