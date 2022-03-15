package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.states.Animation;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class NPCManager implements PacketHandler, PacketUtil {

    protected EntityPlayer npc;
    protected DataWatcher dataWatcher;
    protected String signature = "";
    protected String texture = "";
    protected final UUID uuid = UUID.randomUUID();
    protected final Set<Player> showns = new HashSet<>();
    protected final List<String> listedNameLine = new ArrayList<>();
    protected String mainName = " ";

    protected Map<EnumItemSlot, ItemStack> equips = new EnumMap<>(EnumItemSlot.class);

    public abstract void create(Location location);

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

    }

    public abstract void moveTo(Location location);

    public void lookAt(Location targetLocation) {
        sendHeadRotationPacket(targetLocation);
    }

    public void setSkin(String signature, String texture) {
        this.signature = signature;
        this.texture = texture;
        updateSkin();
    }

    public void setEquipment(EnumItemSlot enumItemSlot, ItemStack itemStack) {
        equips.put(enumItemSlot, itemStack);
        showns.forEach(this::sendEquipmentPacket);
    }

    public void playAnimation(Animation animation) {
        showns.forEach(player -> sendAnimationPacket(player, animation.ordinal()));
    }

}
