package fisherman.behaviors;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class DropItem extends Behavior {

    protected Player target;
    protected int tick;

    public DropItem(NPCManager npcManager) {
        super(npcManager);
        this.forceStartIfNecessary = true;
    }

    @Override
    public boolean check() {
        if(npcManager.getUsedSlotInInventory() == 0) return false;
        if(npcManager.getMemoryModuleIfPresent("giveItemToPlayer") != null) return false;
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld().equals(npcManager.getLocation().getWorld())) {
                if(npcManager.distanceTo(player.getLocation()) < 3) {
                    if(player.isSneaking()) {
                        this.target = player;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void firstAct() {
        this.tick = 20;
        npcManager.putMemoryModule(new MemoryModule<>(MemoryType.WORKING, "giveItemToPlayer", null));
    }

    @Override
    public boolean whileCheck() {
        return this.tick > 0;
    }

    @Override
    public void endAct() {
        npcManager.removeMemoryModule("giveItemToPlayer");
        this.tick = 0;
        this.target = null;
    }

    @Override
    public void act() {
        --this.tick;
        if(this.tick == 1) {
            ItemStack item = Arrays.stream(npcManager.getInventory().getContents()).filter(Objects::nonNull).toList().get(0);
            npcManager.getInventory().removeItem(item);
            Item it = (Item) target.getLocation().getWorld().spawnEntity(npcManager.getNPC().getBukkitEntity().getEyeLocation(), EntityType.DROPPED_ITEM);
            it.setPickupDelay(40);
            it.setItemStack(item);
            it.setVelocity(npcManager.getNPC().getBukkitEntity().getEyeLocation().getDirection().normalize().multiply(0.3));
        }
        npcManager.lookAt(target.getLocation().add(0, 1, 0));
    }
}
