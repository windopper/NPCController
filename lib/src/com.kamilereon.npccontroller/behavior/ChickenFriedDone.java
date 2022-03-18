package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.AIEntity;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.states.ItemSlot;
import net.minecraft.network.protocol.game.PacketPlayOutCollect;
import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class ChickenFriedDone extends Behavior{

    protected Location targetLoc;
    protected boolean getItem = false;

    public ChickenFriedDone() {

    }

    @Override
    public boolean check(NPCManager npcManager) {
        MemoryModule<?> memoryModule = npcManager.getMemoryModuleIfPresent("chickenFried");
        if(memoryModule == null) return false;
        else {
            Location loc = (Location) memoryModule.getData();
            for(Item item : loc.getWorld().getEntitiesByClass(Item.class)) {
                Location itemloc = item.getLocation();
                if(itemloc.distance(loc) >= 2) continue;
                if(item.getItemStack().getType() == Material.COOKED_CHICKEN) {
                    this.targetLoc = loc;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return !getItem;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        npcManager.navigateTo(targetLoc, 1, 2);
    }

    @Override
    public void endAct(NPCManager npcManager) {
        npcManager.setEquipment(ItemSlot.MAIN_HAND, new ItemStack(Material.COOKED_CHICKEN, 1));
        npcManager.removeMemoryModule("chickenFried");
        targetLoc = null;
        getItem = false;
    }
    @Override
    public void act(NPCManager npcManager) {
        Location loc = npcManager.getLocation();
        EntityPlayer npc = npcManager.getNPC();
        if(loc.distance(targetLoc) < 2) {
            for(Item item : loc.getWorld().getEntitiesByClass(Item.class)) {
                Location itloc = item.getLocation();
                if(itloc.distance(loc) >= 2) continue;
                PacketPlayOutCollect packetPlayOutCollect = new PacketPlayOutCollect(item.getEntityId(), npc.getId(), item.getItemStack().getAmount());
                npcManager.getViewers().forEach(p -> npcManager.getPlayerConnection(p).sendPacket(packetPlayOutCollect));
                npcManager.putItem(item.getItemStack());
                item.remove();
            }
            getItem = true;
        }
        else {
            npcManager.navigateTo(targetLoc, 1, 2);
        }
    }
}
