package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class LookAtNearestPlayer extends Behavior {
    private final int radius;
    private final Predicate<Player> predicate;
    public LookAtNearestPlayer(int radius, Predicate<Player> predicate) {
        this.radius = radius;
        this.predicate = predicate;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        Location location = npcManager.getNPC().getBukkitEntity().getLocation();
        for(Player player : Bukkit.getOnlinePlayers()) {
            try {
                if(location.distance(player.getLocation()) < radius) {
                    return true;
                }
            }
            catch(Exception e) {}
        }
        return false;
    }

    @Override
    public void act(NPCManager npcManager) {
        Location location = npcManager.getNPC().getBukkitEntity().getLocation();
        double dist = radius;
        Player nearest = null;
        for(Player player : Bukkit.getOnlinePlayers()) {
            try {
                if(location.distance(player.getLocation()) < radius) {
                    nearest = player;
                    dist = location.distance(player.getLocation());
                }
            }
            catch(Exception e) {}
        }
        if(nearest != null) {
            npcManager.lookAt(nearest.getEyeLocation());
        }
        npcManager.getBehaviorContainer().disableActing(1);
    }
}
