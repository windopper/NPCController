package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.metadata.MetaDataContainer;
import com.kamilereon.npccontroller.states.ItemSlot;
import net.minecraft.network.syncher.DataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface PacketHandler {

    void sendShowPacket(Player player);

    void sendHidePacket(Player player);

    void sendMetadataPacket(Player player);

    void sendMetadataPacket(Player player, MetaDataContainer metaDataContainer);

    void sendEquipmentPacket(Player player);

    void sendEquipmentPacket(Player player, Map<ItemSlot, ItemStack> itemMap);

    void sendAnimationPacket(Player player, int var0);

    void sendHeadRotationPacket(Location targetLocation);
}
