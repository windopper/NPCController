package com.kamilereon.npccontroller.behavior.standard;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class LookAtNearestPlayer extends Behavior {
    private final double radius;
    private final Predicate<Player> predicate;
    private Player target = null;
    private int tick = 0;
    private double percent;

    public LookAtNearestPlayer(NPCManager npcManager, double radius, Predicate<Player> predicate, double percent) {
        super(npcManager);
        this.radius = radius;
        this.predicate = predicate;
        this.percent = percent;
        this.canForceStop = true;
    }

    @Override
    public boolean check() {
        if(NumberUtils.randomDouble(0, 1) > percent) return false;
        Entity npc = npcManager.getAI();
        Location loc = npc.getBukkitEntity().getLocation();
        double nearest = radius;
        target = null;
        for(Player player : Bukkit.getOnlinePlayers()) {
            double dist = loc.distance(player.getLocation());
            if(dist <= nearest && predicate.test(player)) {
                nearest = dist;
                target = player;
            }
        }
        if(target != null) return true;
        return false;
    }

    @Override
    public boolean whileCheck() {
        if(this.target.isDead()) {
           return false;
        } else if(this.target.getLocation().distance(npcManager.getAI().getBukkitEntity().getLocation()) > this.radius) {
            return false;
        } else {
            return this.tick > 0;
        }
    }

    @Override
    public void firstAct() {
        this.tick = 40 + NumberUtils.randomInt(0, 40);
    }

    @Override
    public void endAct() {
        this.target = null;
    }

    @Override
    public void act() {
        npcManager.getAI().getControllerLook().a(target.getLocation().getX(), target.getEyeLocation().getY(), target.getLocation().getZ());
        --this.tick;
    }

    public Player getTarget() { return target; }
}
