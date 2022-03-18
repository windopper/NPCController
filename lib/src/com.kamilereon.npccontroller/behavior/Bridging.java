package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.AIEntity;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Bridging extends Behavior{

    protected double rate;
    protected double x;
    protected double y;
    protected double z;

    public Bridging(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        if(NumberUtils.randomDouble(0, 1) > rate) return false;
        Location loc = npcManager.getLocation();
        Vec3D var = randomPos(npcManager.getAI());
        if(var == null) return false;
        else {
            this.x = var.b;
            this.y = var.c;
            this.z = var.d;

            Location tarLoc = new Location(loc.getWorld(), x, y, z);
            Vector v = tarLoc.toVector().subtract(loc.toVector()).normalize().multiply(0.2);


            return true;
        }
    }

    @Override
    public void act(NPCManager npcManager) {

    }

    private Vec3D randomPos(AIEntity entity) {
        return DefaultRandomPos.a(entity, 10, 7);
    }
}
