package com.kamilereon.npccontroller.behavior.standard;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.pathfinder.PathPoint;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

public class Idle extends Behavior {

    private final double speed;
    private double x;
    private double y;
    private double z;
    private double rate;

    public Idle(NPCManager npcManager, double speed, double rate) {
        super(npcManager);
        this.speed = speed;
        this.rate = rate;
        this.canForceStop = true;
    }

    @Override
    public boolean check() {
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
    public boolean whileCheck() {
        EntityZombie entityZombie = npcManager.getAI();
        return !entityZombie.getNavigation().m() && !entityZombie.isVehicle();
    }

    @Override
    public void firstAct() {
        NavigationAbstract n = npcManager.getAI().getNavigation();
        n.a(this.x, this.y, this.z, this.speed);
    }

    @Override
    public void endAct() {
        npcManager.getAI().getNavigation().o();
    }

    @Override
    public void act() {

    }

    private Vec3D randomPos(EntityZombie entity) {
        return DefaultRandomPos.a(entity, 10, 7);
    }

}
