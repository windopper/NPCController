package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class UseChest extends Behavior{

    protected int radius;
    protected Location targetLoc;
    protected int tick;
    protected double rate;

    public UseChest(int radius, double rate) {
        this.radius = radius;
        this.rate = rate;
    }
    @Override
    public boolean check(NPCManager npcManager) {

        if(npcManager.getRandom().nextDouble() > rate) return false;
        if(npcManager.getInventory().firstEmpty() == 0) return false;

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
        tick = npcManager.getUsedSlotInInventory() * (10 + npcManager.getRandom().nextInt(20));
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
            npcManager.navigateTo(targetLoc, 1.5, 1);
        }
        else {
            --this.tick;
            Chest chest = (Chest) targetLoc.getBlock().getState();

            chest.open();

            if(this.tick % 10 == 0) {
                if(npcManager.getInventory().firstEmpty() != -1) {
                    ItemStack it = Arrays.stream(npcManager.getInventory().getContents()).filter(Objects::nonNull).toList().get(0);
                    npcManager.getInventory().remove(it);
                    chest.getInventory().addItem(it);
                }
            }
        }
    }
}
