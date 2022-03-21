package com.kamilereon.npccontroller;

import net.minecraft.core.BlockPosition;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.IBlockData;

public abstract class AIEntity extends EntityZombie {
    public AIEntity(World world) {
        super(EntityTypes.be, world);
    }

    public abstract int getNoDamageTicks();

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
