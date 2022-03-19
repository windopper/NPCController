package fisherman.behaviors;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.states.Animation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoreFishes extends Behavior {

    protected int radius;
    protected Location targetLoc;
    protected int tick;

    public StoreFishes(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        if(npcManager.getMemoryModuleIfPresent("throw_fishhook") != null) return false;
        if(npcManager.getUsedSlotInInventory() < 4) return false;
        MemoryModule<?> fishChest = npcManager.getMemoryModuleIfPresent("fishchest");

        Location loc = npcManager.getLocation();

        if(fishChest == null) {
            targetLoc = findChest(loc);
            return targetLoc != null;
        } else {
            Location fc = (Location) fishChest.getData();
            targetLoc = fc;
            return true;
        }
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return targetLoc.getBlock().getType() == Material.CHEST && this.tick > 0;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.navigateTo(targetLoc, 1.5, 1);
        this.tick = 40;
    }

    @Override
    public void endAct(NPCManager npcManager) {
        if(targetLoc.getBlock().getType() != Material.CHEST) {
            npcManager.removeMemoryModule("fishchest");
        } else {
            Chest chest = (Chest) targetLoc.getBlock().getState();
            chest.close();
            npcManager.putMemoryModule(new MemoryModule<>("fishchest", targetLoc));
        }

        npcManager.stopNavigating();
        this.tick = 0;
        this.targetLoc = null;
    }

    @Override
    public void act(NPCManager npcManager) {
        if(npcManager.distanceTo(targetLoc) > 3) {
            npcManager.navigateTo(targetLoc, 1.5, 2);
            npcManager.lookAt(targetLoc);
        } else {
            if(this.tick == 40) npcManager.playAnimation(Animation.SWING_MAIN_ARM);
            --this.tick;
            if(targetLoc.getBlock().getType() == Material.CHEST) {
                Chest chest = (Chest) targetLoc.getBlock().getState();
                chest.open();
                npcManager.stopNavigating();
                npcManager.lookAt(targetLoc);
                for(ItemStack itemStack : npcManager.getInventory().getContents()) {
                    if(itemStack == null) continue;
                    if(chest.getInventory().firstEmpty() != -1) {
                        chest.getInventory().addItem(itemStack);
                        npcManager.getInventory().remove(itemStack);
                    }
                }
            }
        }
    }

    private Location findChest(Location loc) {
        List<Location> list = new ArrayList<>();
        for(double x = loc.getX() - radius; x <= loc.getX() + radius; x++) {
            for (double y = loc.getY() - radius; y <= loc.getY() + radius; y++) {
                for (double z = loc.getZ() - radius; z <= loc.getZ() + radius; z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if(l.getBlock().getType() == Material.CHEST) {
                        Chest chest = (Chest) l.getBlock().getState();
                        if(chest.getBlockInventory().firstEmpty() != -1) {
                            list.add(l);
                        }
                    }
                }
            }
        }
        return list.get((new Random()).nextInt(list.size()));
    }
}
