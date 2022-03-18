package com.kamilereon.npccontroller.utils;

import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class NameTagUtils {
    public static EntityArmorStand getArmorStand(NPCManager npcManager, String name, Location loc) {
        EntityArmorStand entityArmorStand = new EntityArmorStand(EntityTypes.c, npcManager.getNPC().getWorld());
        entityArmorStand.setInvisible(true);
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setInvulnerable(true);
        entityArmorStand.setBasePlate(false);
        entityArmorStand.setMarker(true);
        entityArmorStand.setCustomName(new ChatComponentText(name));
        entityArmorStand.setCustomNameVisible(true);
        entityArmorStand.setSmall(true);
        entityArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        npcManager.getAI().getWorld().addEntity(entityArmorStand);
        return entityArmorStand;
    }

    public static ArmorStand getNameTag(Location loc) {
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        return armorStand;
    }
}
