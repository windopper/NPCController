package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.metadata.BehaviorContainer;
import com.kamilereon.npccontroller.metadata.MetaDataContainer;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public abstract class NPCManager implements PacketHandler, PacketUtil, NPCUtil {

    protected EntityPlayer npc;
    protected LivingEntity masterEntity;
    protected Location location;
    protected DataWatcher dataWatcher;
    protected String signature = "";
    protected String texture = "";
    protected final UUID uuid = UUID.randomUUID();
    protected final Set<Player> showns = new HashSet<>();
    protected final List<String> listedNameLine = new ArrayList<>();
    protected String mainName = " ";

    protected boolean destroyed = false;
    protected BukkitTask masterEntityTeleportLoop = null;
    protected BukkitTask physicsTick = null;

    protected Map<EnumItemSlot, ItemStack> equips = new EnumMap<>(EnumItemSlot.class);
    protected final MetaDataContainer metaDataContainer = new MetaDataContainer();
    protected final BehaviorContainer behaviorContainer = new BehaviorContainer(this);

    public NPCManager() {
        this.physicsTick = Bukkit.getScheduler().runTaskTimer(NPCController.plugin, () -> {
            behaviorContainer.judgeNextAction();
        }, 0, 1);
    }

    public abstract void create(Location location);

    public abstract void setVillagerAI();

    public boolean hasAI() { return masterEntityTeleportLoop != null; }

    public EntityPlayer getNPC() { return npc; }

    public Set<Player> getViewers() { return showns; }

    public void setMainName(String var) {
        this.mainName = var;
    }

    public void setNameLine(String ...var) {
        listedNameLine.addAll(Arrays.stream(var).toList());
    }

    public void modifyName(int loc, String var) {
        try {
            listedNameLine.set(loc, var);
        }
        catch(IndexOutOfBoundsException e) {
            NPCController.getInstance().getLogger().severe("Name which you want to modify is out of range");
        }
    }

    public void showTo(Player player) {
        showns.add(player);
        sendShowPacket(player);
    }

    public void hideTo(Player player) {
        showns.remove(player);
        sendHidePacket(player);
    }

    public abstract void updateSkin();

    public void destroy() {
        masterEntity.remove();
        masterEntity = null;
        destroyed = true;
        if(physicsTick != null) physicsTick.cancel();
        Bukkit.getOnlinePlayers().forEach(this::sendHidePacket);
    }

    public boolean isDestroyed() { return destroyed; }

    public abstract void moveTo(Location location);

    public void lookAt(Location targetLocation) {
        sendHeadRotationPacket(targetLocation);
    }

    public void setSkin(String signature, String texture) {
        this.signature = signature;
        this.texture = texture;
        updateSkin();
    }

    public void setEquipment(ItemSlot itemSlot, ItemStack itemStack) {
        equips.put(EnumItemSlot.values()[itemSlot.ordinal()], itemStack);
        showns.forEach(this::sendEquipmentPacket);
    }

    public void playAnimation(Animation animation) {
        showns.forEach(player -> sendAnimationPacket(player, animation.ordinal()));
    }

    public void setStates(States ...states) {
        this.metaDataContainer.setStates(states);
        showns.forEach(this::sendMetadataPacket);
    }

    public void removeStates(States ...states) {
        this.metaDataContainer.removeStates(states);
        showns.forEach(this::sendMetadataPacket);
    }

    public void setPoses(Poses pose) {
        metaDataContainer.setPoses(pose);
        showns.forEach(this::sendMetadataPacket);
    }

    @Override
    public void setBehavior(int priority, BehaviorContainer.EnumBehavior behavior) {
        behaviorContainer.setBehavior(priority, behavior);
    }

    @Override
    public void setBehavior(int priority, Behavior behavior) {
        behaviorContainer.setBehavior(priority, behavior);
    }

    public BehaviorContainer getBehaviorContainer() { return behaviorContainer; }
}
