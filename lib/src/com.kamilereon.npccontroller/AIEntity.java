package com.kamilereon.npccontroller;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.World;

public abstract class AIEntity extends EntityZombie {
    public AIEntity(World world) {
        super(EntityTypes.be, world);
    }
}
