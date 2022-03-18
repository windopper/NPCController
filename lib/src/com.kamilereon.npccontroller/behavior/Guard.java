package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.states.Animation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Guard extends Behavior{

    protected Location center;
    protected int radius;
    protected Player target;

    public Guard(Location location, int radius) {
        this.center = location;
        this.radius = radius;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld().equals(center.getWorld())) {
                Location ploc = player.getLocation();
                if(ploc.distance(center) < radius) {
                    target = player;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return target.getLocation().distance(center) < radius + 2;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.navigateTo(target.getLocation(), 1.5, 3);
    }

    @Override
    public void endAct(NPCManager npcManager) {
        npcManager.navigateTo(center, 1, radius / 2);
    }

    @Override
    public void act(NPCManager npcManager) {
        boolean value = npcManager.navigateTo(target.getLocation(), 1.5, 3);
        if(value) {
            npcManager.lookAt(target.getEyeLocation());
            npcManager.playAnimation(Animation.SWING_MAIN_ARM);
            npcManager.attack(target);
        }
    }
}
