package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;

public class Idle extends Behavior {

    private final double speed;
    private double x;
    private double y;
    private double z;
    private double rate;

    public Idle(double speed, double rate) {
        this.speed = speed;
        this.rate = rate;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        if(NumberUtils.randomDouble(0, 1) > rate) return false;
        Vec3D var = randomPos(npcManager.getAI());
        if(var == null) return false;
        else {
            this.x = var.b;
            this.y = var.c;
            this.z = var.d;
            return true;
        }
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        EntityZombie entityZombie = npcManager.getAI();
        return !entityZombie.getNavigation().m() && !entityZombie.isVehicle();
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.getAI().getNavigation().a(this.x, this.y, this.z, this.speed);
    }

    @Override
    public void endAct(NPCManager npcManager) {
        npcManager.getAI().getNavigation().o();
    }

    @Override
    public void act(NPCManager npcManager) {

    }

    private Vec3D randomPos(EntityZombie entity) {
        return DefaultRandomPos.a(entity, 10, 7);
    }

}
