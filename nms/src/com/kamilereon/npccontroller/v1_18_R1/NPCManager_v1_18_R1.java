package com.kamilereon.npccontroller.v1_18_R1;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.metadata.MetaDataContainer;
import com.kamilereon.npccontroller.states.ItemSlot;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class NPCManager_v1_18_R1 extends NPCManager {
    @Override
    public void create(Location location) {

    }

    @Override
    public void setAI() {

    }

    @Override
    public void updateSkin() {

    }

    @Override
    public void moveTo(Location location) {

    }

    @Override
    public void sendShowPacket(Player player) {

    }

    @Override
    public void sendHidePacket(Player player) {

    }

    @Override
    public void sendMetadataPacket(Player player) {

    }

    @Override
    public void sendMetadataPacket(Player player, MetaDataContainer metaDataContainer) {

    }

    @Override
    public void sendEquipmentPacket(Player player) {

    }

    @Override
    public void sendEquipmentPacket(Player player, Map<ItemSlot, ItemStack> itemMap) {

    }

    @Override
    public void sendAnimationPacket(Player player, int var0) {

    }

    @Override
    public void sendHeadRotationPacket(Location targetLocation) {

    }

    @Override
    public void fixSkinHelmetLayer() {

    }

    @Override
    public PlayerConnection getPlayerConnection(Player player) {
        return null;
    }

    @Override
    public void navigateTo(Location location, double speed) {

    }

    @Override
    public void sit() {

    }

    @Override
    public void unSit() {

    }

    @Override
    public void attack(Player player) {

    }
}
