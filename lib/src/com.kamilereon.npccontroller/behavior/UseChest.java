package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

public class UseChest extends Behavior{

    protected int radius;
    protected Location targetLoc;
    protected int tick;

    public UseChest(int radius) {
        this.radius = radius;
    }
    @Override
    public boolean check(NPCManager npcManager) {

        if(npcManager.getRandom().nextDouble() < 0.8) return false;

        Location loc = npcManager.getLocation();
        for(double x = loc.getX() - radius; x <= loc.getX() + radius; x++) {
            for (double y = loc.getY() - radius; y <= loc.getY() + radius; y++) {
                for (double z = loc.getZ() - radius; z <= loc.getZ() + radius; z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if(l.getBlock().getType() == Material.CHEST) {
                        Chest chest = (Chest) l.getBlock().getState();
                        if(chest.getBlockInventory().firstEmpty() != -1) {
                            targetLoc = l;
                            return true;
                        }
                    }
                }
            }
        }
        return targetLoc != null;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return targetLoc.getBlock().getType() == Material.CHEST && tick > 0;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.navigateTo(targetLoc, 1.5, 2);
        tick = 40;
    }

    @Override
    public void endAct(NPCManager npcManager) {
        if(targetLoc.getBlock().getType() == Material.CHEST) {
            Chest chest = (Chest) targetLoc.getBlock().getState();
            chest.close();
        }
        targetLoc = null;
        tick = 0;
    }

    @Override
    public void act(NPCManager npcManager) {
        Location loc = npcManager.getLocation();
        if(loc.distance(targetLoc) > 2) {
            npcManager.navigateTo(targetLoc, 1.5, 2);
        }
        else {
            --this.tick;
            Chest chest = (Chest) targetLoc.getBlock().getState();
            chest.open();
        }
    }
}
