package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Idle extends Behavior {

    private final int idleDelay;
    private final int bound;
    private final Location loc;

    public Idle(int idleDelay, int bound, Location location) {
        this.idleDelay = idleDelay;
        this.bound = bound;
        this.loc = location;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        return true;
    }

    @Override
    public void act(NPCManager npcManager) {
        Entity npc = npcManager.getNPC().getBukkitEntity();
        Location location = npc.getLocation();
        double yaw = location.getYaw();
        float rpitch = (float) NumberUtils.randomDouble(-10, 10);
        float ryaw = (float) (yaw + NumberUtils.randomDouble(-80, 80));
        location.setYaw(ryaw);
        location.setPitch(rpitch);
        npc.teleport(location);
        npcManager.getBehaviorContainer().disableActing(NumberUtils.randomInt(idleDelay-bound, idleDelay+bound));
    }
}
