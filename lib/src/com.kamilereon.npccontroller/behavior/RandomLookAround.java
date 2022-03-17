package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.world.entity.Entity;

public class RandomLookAround extends Behavior{

    private int tick;
    private double x;
    private double z;

    @Override
    public boolean check(NPCManager npcManager) {
        return NumberUtils.randomDouble(0, 1) < 0.02f;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return this.tick >= 0;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        double var0 = 6.283185307179586D * NumberUtils.randomDouble(0, 1);
        this.x = Math.cos(var0);
        this.z = Math.sin(var0);
        this.tick = 20 + NumberUtils.randomInt(0, 20);
    }

    @Override
    public void act(NPCManager npcManager) {
        --this.tick;
        Entity entity = npcManager.getAI();
        npcManager.getAI().getControllerLook().a(entity.locX() + this.x, entity.getHeadY(), entity.locZ() + this.z);
    }
}
