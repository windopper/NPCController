package com.kamilereon.npccontroller.v1_17_R1;

import net.minecraft.core.BlockPosition;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Zombie;

public class MasterEntity_v1_17_R1 extends EntityZombie {
    public MasterEntity_v1_17_R1(World world) {
        super(world);
    }

    public static EntityZombie summon(Location location) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        MasterEntity_v1_17_R1 entityZombie = new MasterEntity_v1_17_R1(nmsWorld);
        entityZombie.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        Zombie zombie = (Zombie) entityZombie.getBukkitEntity();
        nmsWorld.addEntity(entityZombie);
        zombie.setInvisible(true);
        zombie.teleport(location);
        return entityZombie;
    }

    @Override
    protected void initPathfinder() {

    }

    protected SoundEffect getSoundAmbient() {
        return null;
    }

    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ot;
    }

    protected SoundEffect getSoundDeath() {
        return SoundEffects.os;
    }

    protected SoundEffect getSoundStep() {
        return null;
    }

    protected void b(BlockPosition blockposition, IBlockData iblockdata) {

    }
}
