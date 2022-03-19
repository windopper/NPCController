package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.utils.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class FarmingWheat extends Behavior{

    protected int radius;
    protected Set<Location> farmlands = new HashSet<>();
    protected Set<Location> seedLocs = new HashSet<>();
    protected Location preTarget;
    protected Location targetLoc;
    protected int tick = 0;

    public FarmingWheat(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean check(NPCManager npcManager) {

        Location loc = npcManager.getLocation();

        if(npcManager.getInventory().contains(Material.WHEAT_SEEDS)) {
            for(double x = loc.getX() - radius; x <= loc.getX() + radius; x++) {
                for(double y = loc.getY() - radius; y <= loc.getY() + radius; y++) {
                    for(double z = loc.getZ() - radius; z <= loc.getZ() + radius; z++) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        if(l.getBlock().getType() == Material.FARMLAND) {
                            if(l.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                                farmlands.add(l);
                            }
                        }
                    }
                }
            }
        }

        if(farmlands.size() > 0) {
            this.targetLoc = farmlands.stream().toList().get(NumberUtils.randomInt(0, farmlands.size()-1)).add(0, 1, 0);
            return true;
        }

        if(npcManager.getRandom().nextDouble() > 0) {
            for(Location l : seedLocs) {
                if(l.getBlock().getType() == Material.WHEAT) {
                    Ageable age = (Ageable) l.getBlock().getBlockData();
                    if(age.getMaximumAge() == age.getAge()) {
                        this.targetLoc = l;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        if(npcManager.getLocation().distance(targetLoc) < 3 && tick <= 0) return false;
        return true;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        this.tick = 40;
        npcManager.setEquipment(ItemSlot.MAIN_HAND, new ItemStack(Material.WHEAT_SEEDS, 1));
        npcManager.navigateTo(targetLoc, 1.2, 2);
    }

    @Override
    public void endAct(NPCManager npcManager) {

        if(targetLoc.getBlock().getType() == Material.AIR) {
            npcManager.playAnimation(Animation.SWING_MAIN_ARM);
            npcManager.playSound(targetLoc, Sound.ITEM_CROP_PLANT, 1, 1);
            BlockData data = Material.WHEAT.createBlockData();
            Ageable age = (Ageable) data;
            age.setAge(1);
            npcManager.getInventory().removeItem(new ItemStack(Material.WHEAT_SEEDS, 1));
            this.targetLoc.getBlock().setBlockData(age);
            this.seedLocs.add(targetLoc);
        } else if(targetLoc.getBlock().getType() == Material.WHEAT){
            Ageable age = (Ageable) targetLoc.getBlock().getBlockData();
            if(age.getMaximumAge() == age.getAge()) {
                npcManager.playAnimation(Animation.SWING_MAIN_ARM);
                npcManager.spawnParticle(Particle.BLOCK_DUST, targetLoc.clone().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0, age.getMaterial().createBlockData());
                npcManager.playSound(targetLoc, Sound.BLOCK_CROP_BREAK, 1, 1);
                targetLoc.getWorld().dropItem(targetLoc, new ItemStack(Material.WHEAT, 1));
                targetLoc.getWorld().dropItem(targetLoc, new ItemStack(Material.WHEAT_SEEDS, npcManager.getRandom().nextInt(3)+1));
                targetLoc.getBlock().setType(Material.AIR);
            }
        }

        this.targetLoc = null;
        this.farmlands.clear();
    }

    @Override
    public void act(NPCManager npcManager) {
        if(npcManager.getLocation().distance(targetLoc) >= 3) {
            npcManager.navigateTo(targetLoc, 1.2, 2);
        } else {
            --this.tick;
            npcManager.lookAt(targetLoc);
        }
    }
}
