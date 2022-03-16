package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.utils.NumberUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Idle extends Behavior {

    private final int idleDelay;
    private final int bound;

    public Idle(int idleDelay, int bound) {
        this.idleDelay = idleDelay;
        this.bound = bound;
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
        double pitch = location.getPitch();
        float rpitch = (float) NumberUtils.randomDouble(-20, 20);
        float ryaw = (float) (yaw + NumberUtils.randomDouble(-40, 40));
        location.setYaw(ryaw);
        location.setPitch(rpitch);
        npc.teleport(location);
        npcManager.getBehaviorContainer().disableActing(NumberUtils.randomInt(idleDelay-bound, idleDelay+bound));
    }
}
