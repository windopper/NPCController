package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.network.protocol.game.PacketPlayOutCollect;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class PickUpItem extends Behavior {

    private Predicate<Item> predicate;
    private Set<Item> itemPicks = new HashSet<>();
    private double radius;

    public PickUpItem(double radius, Predicate<Item> predicate) {
        this.predicate = predicate;
        this.radius = radius;
    }

    @Override
    public boolean check(NPCManager npcManager) {
        EntityZombie npc = npcManager.getAI();
        Location loc = npc.getBukkitEntity().getLocation();
        for(Item item : loc.getWorld().getEntitiesByClass(Item.class)) {
            if(!predicate.test(item)) continue;
            Location eloc = item.getLocation();
            if(eloc.distance(loc) < radius) {
                itemPicks.add(item);
            }
        }
        return itemPicks.size() >= 1;
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return false;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        EntityZombie npc = npcManager.getAI();
        for(Item item : itemPicks) {
            if(npcManager.isInventoryFull()) return;
            PacketPlayOutCollect packetPlayOutCollect = new PacketPlayOutCollect(item.getEntityId(), npc.getId(), item.getItemStack().getAmount());
            npcManager.getViewers().forEach(p -> npcManager.getPlayerConnection(p).sendPacket(packetPlayOutCollect));
            npcManager.putItem(item.getItemStack());
            item.remove();
        }
    }

    @Override
    public void endAct(NPCManager npcManager) {
        itemPicks.clear();
    }

    @Override
    public void act(NPCManager npcManager) {

    }
}
