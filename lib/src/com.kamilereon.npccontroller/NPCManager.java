package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.metadata.BehaviorContainer;
import com.kamilereon.npccontroller.metadata.MetaDataContainer;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.animal.horse.EntityHorse;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public abstract class NPCManager implements PacketHandler, PacketUtil, NPCAIUtil {

    protected EntityPlayer npc;
    protected EntityZombie masterEntity;
    protected EntityHorse chair;
    protected Location location;
    protected DataWatcher dataWatcher;
    protected String signature = "";
    protected String texture = "";
    protected final UUID uuid = UUID.randomUUID();
    protected final Set<Player> showns = new HashSet<>();
    protected final List<String> listedNameLine = new ArrayList<>();
    protected String mainName = " ";

    protected boolean destroyed = false;
    protected boolean canPickUpItem = false;
    protected BukkitTask masterEntityTeleportLoop = null;
    protected BukkitTask physicsTick = null;

    protected Map<EnumItemSlot, ItemStack> equips = new EnumMap<>(EnumItemSlot.class);
    protected final List<ItemStack> inventory = new ArrayList<>();
    protected final Map<ItemStack, Double> dropTable = new HashMap<>();
    protected final MetaDataContainer metaDataContainer = new MetaDataContainer();
    protected final BehaviorContainer behaviorContainer = new BehaviorContainer(this);

    protected final Random random = new Random();

    public NPCManager() {
        this.physicsTick = Bukkit.getScheduler().runTaskTimer(NPCController.plugin, () -> {
            behaviorContainer.behaviorProcess();
            if(masterEntity != null) {
                if(!masterEntity.isAlive()) destroy();
                else {
                    Location loc = masterEntity.getBukkitEntity().getLocation();
                    this.npc.getBukkitEntity().teleport(loc);
                }
            }
        }, 0, 1);
    }

    public abstract void create(Location location);

    public abstract void setAI();

    public EntityZombie getAI() { return masterEntity; }

    public Location getLocation() { return npc.getBukkitEntity().getLocation(); }

    public boolean hasAI() { return masterEntityTeleportLoop != null; }

    public EntityPlayer getNPC() { return npc; }

    public Set<Player> getViewers() { return showns; }

    public void putItem(ItemStack itemStack) {
        if(isInventoryFull()) return;
        inventory.add(itemStack);
    }

    public boolean isInventoryFull() { return inventory.size() > 45; }

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

        for(ItemStack item : dropTable.keySet()) {
            if(random.nextDouble() > dropTable.get(item)) continue;
            location.getWorld().dropItem(getLocation(), item);
        }

        masterEntity.setRemoved(Entity.RemovalReason.a);
        npc.setRemoved(Entity.RemovalReason.a);
        masterEntity = null;
        destroyed = true;

        if(physicsTick != null) physicsTick.cancel();
        Bukkit.getOnlinePlayers().forEach(this::sendHidePacket);
        NPCController.removeNPCManager(this);
    }

    public boolean isDestroyed() { return destroyed; }

    public abstract void moveTo(Location location);

    public void lookAt(Location targetLocation) {
        masterEntity.getControllerLook().a(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
    }

    public void setCanPickUpItem(boolean var) { this.canPickUpItem = var; }

    public void addDropTable(ItemStack itemStack, double dropRate) {
        this.dropTable.put(itemStack, dropRate);
    }

    public void addDropTable(Map<ItemStack, Double> dropTable) {
        this.dropTable.putAll(dropTable);
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
    public void setBehavior(int priority, Behavior behavior) {
        behaviorContainer.setBehavior(priority, behavior);
    }

    public BehaviorContainer getBehaviorContainer() { return behaviorContainer; }

    @Override
    public void navigateTo(Location location, double speed) {
        if(this.masterEntity == null) return;
        EntityZombie ev = this.masterEntity;
        ev.getNavigation().a(location.getX(), location.getY(), location.getZ(), speed);
    }

    @Override
    public void jump() {
        this.masterEntity.getControllerJump().jump();
    }

    public abstract void attack(Entity target);

    public abstract void sit();

    public abstract void unSit();
}
