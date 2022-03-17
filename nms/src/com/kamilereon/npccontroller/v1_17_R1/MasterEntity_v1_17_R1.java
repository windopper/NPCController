package com.kamilereon.npccontroller.v1_17_R1;

import com.google.common.collect.Sets;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Villager;

import java.lang.reflect.Field;
import java.util.Collections;

public class MasterEntity_v1_17_R1 extends EntityVillager {
    private MasterEntity_v1_17_R1(World world) {
        super(EntityTypes.aV, world);
    }

    public static Villager summon(Location location) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        MasterEntity_v1_17_R1 entityVillager = new MasterEntity_v1_17_R1(nmsWorld);
        entityVillager.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

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

    private void removeAI() {
        try {
        Field availableGoalsField = PathfinderGoalSelector.class.getDeclaredField("d");
        Field priorityBehaviorsField = BehaviorController.class.getDeclaredField("e");
//        Field coreActivitysField = BehaviorController.class.getDeclaredField("i");

        availableGoalsField.setAccessible(true);
        priorityBehaviorsField.setAccessible(true);
//        coreActivitysField.setAccessible(true);

        availableGoalsField.set(this.bP, Sets.newLinkedHashSet());
        availableGoalsField.set(this.bQ, Sets.newLinkedHashSet());
        priorityBehaviorsField.set(this.getBehaviorController(), Collections.emptyMap());
//        coreActivitysField.set(this.getBehaviorController(), Sets.newHashSet());

        } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }


}
