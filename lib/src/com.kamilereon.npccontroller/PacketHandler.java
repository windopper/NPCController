package com.kamilereon.npccontroller;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PacketHandler {

    void sendShowPacket(Player player);

    void sendHidePacket(Player player);

    void sendMetadataPacket(Player player);

    void sendEquipmentPacket(Player player);

    void sendAnimationPacket(Player player, int var0);

    void sendHeadRotationPacket(Location targetLocation);
}
