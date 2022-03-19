package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.states.Animation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Campfire;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class ChickenFried extends Behavior{

    protected int radius;
    protected double rate;
    protected Location targetLoc;
    protected Campfire targetCampFire;
    protected int tick;

    public ChickenFried(int radius, double rate) {
        this.radius = radius;
        this.rate = rate;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        if(npcManager.getRandom().nextDouble() > rate) return false;
        Location loc = npcManager.getLocation();
        for(double x = loc.getX() - radius; x <= loc.getX() + radius; x++) {
            for(double y = loc.getY() - radius; y <= loc.getY() + radius; y++) {
                for(double z = loc.getZ() - radius; z <= loc.getZ() + radius; z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if(l.getBlock().getType() == Material.CAMPFIRE) {
                        Campfire campfire = (Campfire) l.getBlock().getState();
                        boolean hasEmpty = false;
                        for(int i=0; i<4; i++) {
                            if(campfire.getItem(i) == null) hasEmpty = true;
                        }
                        if(hasEmpty) {
                            targetLoc = l;
                            targetCampFire = campfire;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return this.tick > 0;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.navigateTo(targetLoc, 1, 2);
        this.tick = 20;
    }

    @Override
    public void endAct(NPCManager npcManager) {
        npcManager.putMemoryModule(new MemoryModule<>("chickenFried", targetLoc));
        targetLoc = null;
        targetCampFire = null;
        this.tick = 0;
    }

    @Override
    public void act(NPCManager npcManager) {
        Location loc = npcManager.getLocation();
        if(targetLoc.distance(loc) <= 3) {
            npcManager.lookAt(targetLoc);
            if(this.tick == 1) {
                for(int i=0; i<4; i++) {
                    if(targetCampFire.getItem(i) == null) {
                        targetCampFire.setCookTime(i, 0);
                        targetCampFire.setCookTimeTotal(i, 100);
                        targetCampFire.setItem(i, new ItemStack(Material.CHICKEN, 1));
                        targetCampFire.update();
                        npcManager.playAnimation(Animation.SWING_MAIN_ARM);
                        npcManager.playSound(targetLoc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                        this.tick = 20;
                        break;
                    }
                }
            }
            --this.tick;
        } else {
            npcManager.navigateTo(targetLoc, 1, 1);
        }
    }
}
