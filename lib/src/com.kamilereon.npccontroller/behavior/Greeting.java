package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.utils.NumberUtils;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.function.Predicate;

public class Greeting extends Behavior{

    private Predicate<Player> predicate;
    private double rate;
    private int tick = 0;

    public Greeting(double rate, Predicate<Player> predicate) {
        this.rate = rate;
        this.predicate = predicate;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        if(NumberUtils.randomDouble(0, 1) >= rate) return false;
        EntityZombie npc = npcManager.getAI();
        for(Player player : Bukkit.getOnlinePlayers()) {
            Location loc = player.getLocation();
            if(npc.getBukkitEntity().getLocation().distance(loc) < 6) {
                if(player.isSneaking()) return true;
            }
        }
        return false;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        if(this.tick < 0) return false;
        EntityZombie npc = npcManager.getAI();
        for(Player player : Bukkit.getOnlinePlayers()) {
            Location loc = player.getLocation();
            if(npc.getBukkitEntity().getLocation().distance(loc) < 6) {
                if(player.isSneaking()) return true;
            }
        }
        return false;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.setPoses(Poses.SNEAKING);
        this.tick = 20 + NumberUtils.randomInt(0, 20);
    }

    @Override
    public void endAct(NPCManager npcManager) {
        npcManager.setPoses(Poses.STANDING);
        super.endAct(npcManager);
    }

    @Override
    public void act(NPCManager npcManager) {
        --this.tick;
    }
}
