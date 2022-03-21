package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import com.kamilereon.npccontroller.metadata.BehaviorContainer;
import com.kamilereon.npccontroller.metadata.MetaDataContainer;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import com.kamilereon.npccontroller.utils.NameTagUtils;
import net.minecraft.network.protocol.game.PacketPlayOutCollect;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.animal.horse.EntityHorse;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public abstract class NPCManager implements PacketHandler, PacketUtil, NPCAIUtil {

    protected EntityPlayer npc;
    protected AIEntity masterEntity;
    protected EntityHorse chair;
    protected Location location;
    protected DataWatcher dataWatcher;
    protected String signature = "";
    protected String texture = "";
    protected final UUID uuid = UUID.randomUUID();
    protected final Set<Player> showns = Collections.synchronizedSet(new HashSet<>()); // npc 를 볼 수 있는 권한이 있는 사람들
    protected boolean showToAll = false;
    protected final Set<Player> farShowns = Collections.synchronizedSet(new HashSet<>()); // 이 npc 를 볼 권한은 있지만 너무 멀어서 볼 수 없음
    protected final HashMap<String, ArmorStand> listedNameLine = new HashMap<>(); // 이름
    protected String mainName = " ";

    protected boolean destroyed = false; // npc 가 파괴되었는지?
    protected boolean canPickUpItem = false; // 아이템을 주울 수 있는지
    protected BukkitTask masterEntityTeleportLoop = null;
    protected int physicsTick; // 1틱에 한번 루프 ( 생성자 초기화 )
    protected int normalPhysicsTick;
    protected int LazyPhysicsTick; // 10틱에 한번 루프 ( 생성자 초기화 )

    protected Map<EnumItemSlot, ItemStack> equips = new EnumMap<>(EnumItemSlot.class); // 장비
    protected final Inventory inventory = Bukkit.createInventory(null, 45, " ");
//    protected final List<ItemStack> inventory = new ArrayList<>(); // 인벤토리
    protected final Map<ItemStack, Double> dropTable = new HashMap<>(); // 드랍테이블 & 확률
    protected final MetaDataContainer metaDataContainer = new MetaDataContainer(); // 메타데이터 패킷
    protected final BehaviorContainer behaviorContainer = new BehaviorContainer(this); // npc 인공지능
    protected final Set<MemoryModule<?>> memories = new HashSet<>();

    protected final Random random = new Random();

    protected int despawnRadius = 100;

    public NPCManager() {
        this.physicsTick = new BukkitRunnable() {
            @Override
            public void run() {
                if(isDestroyed()) {
                    cancel();
                }
                if(showns.size() == 0 && !isDestroyed()) {
                    destroy();
                    cancel();
                }

                behaviorContainer.behaviorProcess();

                if(masterEntity != null) {
                    if (masterEntity.getBukkitEntity().isDead()) {
                        destroy();
                        cancel();
                    }
                    else {

                        if(showToAll) { showns.addAll(Bukkit.getOnlinePlayers()); }

                        // Teleport Loop
                        Location loc = masterEntity.getBukkitEntity().getLocation();
                        getNPC().getBukkitEntity().teleport(loc);

                        // Name Tag Loop
                        double c = 0;
                        double height = masterEntity.getBukkitEntity().getHeight();
                        Location eloc = getNPC().getBukkitEntity().getLocation();
                        for (ArmorStand e : listedNameLine.values()) {
                            Location cloc = eloc.clone();
                            cloc.setY(npc.getBukkitEntity().getEyeHeight() + c);
                            e.teleport(getNPC().getBukkitEntity().getLocation().add(0, c + height, 0));
                            c += 0.25;
                        }

                        // FarPlayer Loop
                        Iterator<Player> it = farShowns.iterator();
                        while(it.hasNext()) {
                            Player p = it.next();
                            p.getNoDamageTicks();
                            if(loc.distance(p.getLocation()) < 35) {
                                updateForFarShowns(p);
                                it.remove();
                            }
                        }

                        // PickUp Item
                        if(canPickUpItem) {
                            if(!isInventoryFull()) {
                                for(Item item : loc.getWorld().getEntitiesByClass(Item.class)) {
                                    Location itemloc = item.getLocation();
                                    if(itemloc.distance(loc) >= 2) continue;
                                    if(item.getPickupDelay() > 0) continue;
                                    PacketPlayOutCollect packetPlayOutCollect = new PacketPlayOutCollect(item.getEntityId(), npc.getId(), item.getItemStack().getAmount());
                                    getViewers().forEach(p -> getPlayerConnection(p).sendPacket(packetPlayOutCollect));
                                    putItemToInventory(item.getItemStack());
                                    item.remove();
                                }
                            }
                        }

                        // MasterEntity damaged
                        if(masterEntity.getNoDamageTicks() > 0) {
                            if(!metaDataContainer.isRed) {
                                playAnimation(Animation.TAKE_DAMAGE);
                                metaDataContainer.isRed = true;
                            }
                        }
                        else if(metaDataContainer.isRed) metaDataContainer.isRed = false;

                        for(ExperienceOrb orb : loc.getWorld().getEntitiesByClass(ExperienceOrb.class)) {
                            orb.remove();
                        }
                    }
                }
            }
        }.runTaskTimer(NPCController.plugin, 0, 1).getTaskId();

        this.LazyPhysicsTick = new BukkitRunnable() {
            @Override
            public void run() {
                if(isDestroyed()) {
                    cancel();
                    return;
                }
                boolean inRadius = false;
                Location loc = getLocation();
                for (Player shown : getViewers()) {
                    Location ploc = shown.getLocation();
                    try {
                        if(loc.distance(ploc) < despawnRadius) inRadius = true;
                        break;
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                if(!inRadius) {
                    destroy();
                    cancel();
                }
            }
        }.runTaskTimer(NPCController.plugin, 0, 10).getTaskId();
    }

    public abstract void create(Location location);

    public void create(Location location, String name) {
        this.mainName = name;
        create(location);
    }

    public void create(Location location, String name, String signature, String texture) {
        this.mainName = name;
        this.texture = texture;
        this.signature = signature;
        create(location);
    }

    public abstract void setAI();

    public AIEntity getAI() { return masterEntity; }

    public boolean hasAI() { return masterEntityTeleportLoop != null; }

    public EntityPlayer getNPC() { return npc; }

    public void setDespawnRadius(int despawnRadius) { this.despawnRadius = despawnRadius; }

    public Location getLocation() { return npc.getBukkitEntity().getLocation(); }

    public Random getRandom() { return random; }

    public <T> T getRandomOne(List<T> list) {
        if(list.size() == 0) return null;
        return list.get(getRandom().nextInt(list.size()));
    }



    // MemoryModule Part

    public MemoryModule<?> getMemoryModuleIfPresent(String key) {
        for(MemoryModule<?> memoryKey : memories) {
            if(memoryKey.getMemoryKey().equals(key)) {
                return memoryKey;
            }
        }
        return null;
    }

    public void putMemoryModule(MemoryModule<?> memoryModule) {
        memories.removeIf(m -> m.getMemoryKey().equals(memoryModule.getMemoryKey()));
        memories.add(memoryModule);
    }

    public void removeMemoryModule(String key) {
        memories.removeIf(memoryModule -> memoryModule.getMemoryKey().equals(key));
    }

    public void forgetMemory(MemoryType memoryType) {
        memories.removeIf(m -> m.getMemoryType().equals(memoryType));
    }

    //

    /** Inventory Part */

    public void putItemToInventory(ItemStack itemStack) {
        if(isInventoryFull()) return;
        inventory.addItem(itemStack);
    }

    public ItemStack removeAndGetItemFromInventory(int var) {
        ItemStack itemStack = inventory.getItem(var);
        inventory.remove(itemStack);
        return itemStack;
    }

    public boolean isInventoryFull() { return inventory.firstEmpty() == -1; }

    public Inventory getInventory() { return inventory; }

    public int getUsedSlotInInventory() {
        return Arrays.stream(inventory.getContents()).filter(Objects::nonNull).toList().size();
    }

    public int getUsedSlotInInventory(Material material) {
        return Arrays.stream(inventory.getContents()).filter(Objects::nonNull).filter(i -> i.getType() == material).toList().size();
    }

    /** */

    public void setMainName(String var) {
        this.mainName = var;
    }

    public void setNameLine(String ...var) {
        double i = 0;
        double h = this.npc.getBukkitEntity().getHeight();
        for(String name : var) {
            ArmorStand armorStand = NameTagUtils.getNameTag(this.npc.getBukkitEntity().getLocation().add(0, h+i, 0));
            armorStand.setCustomName(name);
            listedNameLine.put(name, armorStand);
            i+=0.25;
        }
    }

    public Set<Player> getViewers() {
        if(showToAll) {
            return new HashSet<>(Bukkit.getOnlinePlayers());
        }
        return showns;
    }

    public void addFarShowns(Player player) {
        farShowns.add(player);
    }

    public void removeFarShowns(Player player) {
        farShowns.remove(player);
    }

    public void showToAll() {
        this.showToAll = true;
        showns.addAll(Bukkit.getOnlinePlayers());
        getViewers().forEach(this::sendShowPacket);
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
        if(destroyed) return;

        destroyed = true;

        for(ItemStack item : dropTable.keySet()) {
            if(random.nextDouble() > dropTable.get(item)) continue;
            location.getWorld().dropItem(getLocation(), item);
        }

        for(ArmorStand a : listedNameLine.values()) {
            a.remove();
        }
        if(masterEntity != null) masterEntity.setRemoved(Entity.RemovalReason.a);
        if(npc != null) npc.setRemoved(Entity.RemovalReason.a);

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
//        npc.getBukkitEntity().getEquipment().setItem(EquipmentSlot.values()[itemSlot.ordinal()], itemStack);
        getViewers().forEach(this::sendEquipmentPacket);
    }

    public void playAnimation(Animation animation) {
        getViewers().forEach(player -> sendAnimationPacket(player, animation.ordinal()));
    }

    public void setStates(States ...states) {
        this.metaDataContainer.setStates(states);
        getViewers().forEach(this::sendMetadataPacket);
    }

    public void removeStates(States ...states) {
        this.metaDataContainer.removeStates(states);
        getViewers().forEach(this::sendMetadataPacket);
    }

    public void setPoses(Poses pose) {
        metaDataContainer.setPoses(pose);
        getViewers().forEach(this::sendMetadataPacket);
    }

    public void triggerHand(boolean value) {
        // Hand states, used to trigger blocking/eating/drinking animation.
        this.metaDataContainer.setHandState(value);
        getViewers().forEach(this::sendMetadataPacket);
    }

    public boolean isHandTriggered() {
        return this.metaDataContainer.getHandState();
    }

    public void updateForFarShowns(Player player) {
        sendShowPacket(player);

        sendEquipmentPacket(player);
        sendMetadataPacket(player);
    }

    @Override
    public void setBehavior(int priority, Behavior behavior) {
        behaviorContainer.setBehavior(priority, behavior);
    }

    @Override
    public void setBehavior(int priority, Behavior behavior, boolean forceStart) {

    }

    public void playSound(Location location, Sound sound, float volume, float pitch) {
        for(Player player : getViewers()) {
            if(player.getWorld().equals(location.getWorld())) {
                player.getWorld().playSound(location, sound, volume, pitch);
            }
        }
    }

    public <T> void spawnParticle(Particle particle, Location location, int amount, double x, double y, double z, double speed, T t) {
        for(Player player : getViewers()) {
            if(player.getWorld().equals(location.getWorld())) {
                player.getWorld().spawnParticle(particle, location, amount, x, y, z, t);
            }
        }
    }

    public BehaviorContainer getBehaviorContainer() { return behaviorContainer; }

    @Override
    public boolean navigateTo(Location location, double speed, int nearbyDist) {
        if(this.masterEntity == null) return false;
        AIEntity ev = this.masterEntity;
        PathEntity pe = ev.getNavigation().a(location.getX(), location.getY(), location.getZ(), nearbyDist);
        return !ev.getNavigation().a(pe, speed);
    }

    public void stopNavigating() {
        this.masterEntity.getNavigation().o();
    }

    public double distanceTo(Location location) {
        return location.distance(getLocation());
    }

    @Override
    public void jump() {
        this.masterEntity.getControllerJump().jump();
    }

    public void leap(double power) {
        Vec3D var0 = this.getAI().getMot();
        Vector v = this.getAI().getBukkitEntity().getLocation().getDirection();
        Vec3D var1 = new Vec3D(v.getX(), 0.0D, v.getZ());
        if(var1.g() > 1.0E-7D) {
            var1 = var1.d().a(power).e(var0.a(0.2D));
        }
        this.getAI().setMot(var1.b, 0.2D, var1.d);
    }
}
