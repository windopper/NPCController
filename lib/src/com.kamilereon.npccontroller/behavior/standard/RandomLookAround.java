package com.kamilereon.npccontroller.behavior.standard;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.NPC;

public class RandomLookAround extends Behavior {


    private int tick;
    private double x;
    private double z;

    public RandomLookAround(NPCManager npcManager) {
        super(npcManager);
        this.canForceStop = true;
    }

    @Override
    public boolean check() {
        return NumberUtils.randomDouble(0, 1) < 0.02f;
    }

    @Override
    public boolean whileCheck() {
        return this.tick >= 0;
    }

    @Override
    public void firstAct() {
        double var0 = 6.283185307179586D * NumberUtils.randomDouble(0, 1);
        this.x = Math.cos(var0);
        this.z = Math.sin(var0);
        this.tick = 20 + NumberUtils.randomInt(0, 20);
    }

    @Override
    public void act() {
        --this.tick;
        Entity entity = npcManager.getAI();
        npcManager.getAI().getControllerLook().a(entity.locX() + this.x, entity.getHeadY(), entity.locZ() + this.z);
    }
}
