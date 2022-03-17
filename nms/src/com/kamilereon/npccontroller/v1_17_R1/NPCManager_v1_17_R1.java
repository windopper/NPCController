package com.kamilereon.npccontroller.v1_17_R1;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.NPCControllerMain;
import com.kamilereon.npccontroller.states.Animation;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCManager_v1_17_R1 extends NPCManager {

    @Override
    public void create(Location location) {
        this.location = location;
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), mainName);
        Property property = new Property("textures", texture, signature);
        gameProfile.getProperties().put("textures", property);

        this.npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile);

        this.npc.b = new PlayerConnection(nmsServer, new NetworkManager(EnumProtocolDirection.a), npc);
        nmsWorld.addEntity(this.npc);
        this.npc.getBukkitEntity().setCollidable(false);
        this.npc.getBukkitEntity().setInvulnerable(true);
        this.dataWatcher = this.npc.getDataWatcher();
        this.npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.npc.setCustomName(new ChatComponentText(mainName));
    }

    @Override
    public void setAI() {
        if(this.masterEntity != null) return;
        this.masterEntity = MasterEntity_v1_17_R1.summon(location);
    }

    @Override
    public void updateSkin() {
        GameProfile newGameProfile = this.npc.getProfile();
        newGameProfile.getProperties().get("textures").clear();
        newGameProfile.getProperties().put("textures", new Property("textures", texture, signature));
        showns.forEach(this::sendHidePacket);
        Bukkit.getScheduler().runTaskLater(NPCController.plugin, () -> showns.forEach(this::sendShowPacket), 10);
    }

    @Override
    public void moveTo(Location location) {

    }

    @Override
    public void sendShowPacket(Player player) {
        PlayerConnection playerConnection = getPlayerConnection(player);

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a,
                npc
        );
        PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawn(
                npc
        );

        playerConnection.sendPacket(packetPlayOutPlayerInfo);
        playerConnection.sendPacket(packetPlayOutNamedEntitySpawn);

        Bukkit.getServer().getScheduler().runTaskLater(NPCControllerMain.getPlugin(NPCControllerMain.class), () -> {
            PacketPlayOutPlayerInfo removeFromTabPacket = new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e,
                    npc
            );
            playerConnection.sendPacket(removeFromTabPacket);
            fixSkinHelmetLayer();
        }, 1);
    }

    @Override
    public void sendHidePacket(Player player) {
        getPlayerConnection(player).sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
    }

    @Override
    public void sendMetadataPacket(Player player) {
        byte i = 0x00;
        for(byte b : metaDataContainer.getStates().values()) {
            i |= b;
        }
        this.dataWatcher.set(DataWatcherRegistry.a.a(0), i);
        this.dataWatcher.set(DataWatcherRegistry.s.a(6), EntityPose.values()[metaDataContainer.getPose().getVarInt()]);
        getPlayerConnection(player).sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), dataWatcher, true));
    }

    @Override
    public void sendEquipmentPacket(Player player) {
        List<Pair<EnumItemSlot, ItemStack>> pairs = new ArrayList<>();
        for(EnumItemSlot enumItemSlot : equips.keySet()) {
            this.npc.getBukkitEntity().getEquipment().setItem(EquipmentSlot.values()[enumItemSlot.ordinal()], equips.get(enumItemSlot));
            Pair<EnumItemSlot, ItemStack> pair = new Pair<>(enumItemSlot, CraftItemStack.asNMSCopy(this.equips.get(enumItemSlot)));
            pairs.add(pair);
        }
//        getPlayerConnection(player).sendPacket(new PacketPlayOutEntityEquipment(npc.getId(), pairs));
    }


    @Override
    public void sendAnimationPacket(Player player, int var0) {
        getPlayerConnection(player).sendPacket(new PacketPlayOutAnimation(npc, var0));
    }

    @Override
    public void sendHeadRotationPacket(Location targetLocation) {
        Location origin = this.npc.getBukkitEntity().getEyeLocation();
        Vector dirBetween = targetLocation.toVector().subtract(origin.toVector());
        origin.setDirection(dirBetween);

        float yaw = origin.getYaw();
        float pitch = origin.getPitch();

        PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook
                = new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) ((yaw % 360.) * 256 / 360), (byte) ((pitch % 360.) * 256 / 360), false);
        PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation
                = new PacketPlayOutEntityHeadRotation(npc, (byte) (yaw * 256 / 360));

        Bukkit.getOnlinePlayers().forEach(player -> {
            getPlayerConnection(player).sendPacket(packetPlayOutEntityHeadRotation);
            getPlayerConnection(player).sendPacket(packetPlayOutEntityLook);
        });
    }

    @Override
    public void fixSkinHelmetLayer() {
        this.dataWatcher.set(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);
        showns.forEach(this::sendMetadataPacket);
    }

    @Override
    public PlayerConnection getPlayerConnection(Player player) {
        return ((CraftPlayer) player).getHandle().b;
    }

    @Override
    public void attack(Entity target) {

    }

    @Override
    public void sit() {

    }

    @Override
    public void unSit() {

    }

    @Override
    public void attack(org.bukkit.entity.Entity target) {
        this.playAnimation(Animation.SWING_MAIN_ARM);
        masterEntity.attackEntity(((CraftEntity) target).getHandle());
    }
}
