package com.kamilereon.npccontroller;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class NPCManager {

    public abstract void create();

    public abstract void showTo(Player player);

    public abstract void hideTo(Player player);

    public abstract void destroy();

    public abstract void moveTo(Location location);

    public abstract void lookAt(Location location);

    public abstract void setSkin(String signature, String value);

    public abstract void setEquipment(EquipmentSlot equipment, ItemStack itemStack);

}
