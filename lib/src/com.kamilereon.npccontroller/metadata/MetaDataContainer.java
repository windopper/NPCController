package com.kamilereon.npccontroller.metadata;

import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class MetaDataContainer implements Cloneable {
    private Map<States, Byte> states = new EnumMap<>(States.class);
    private Poses pose = Poses.STANDING;
    private boolean isHandTriggered = false; // Hand states, used to trigger blocking/eating/drinking animation.

    public void setStates(States ...states) {
        Arrays.stream(states).forEach(K -> this.states.put(K, K.getBitMask()));
    }

    public void removeStates(States ...states) {
        Arrays.stream(states).forEach(K -> this.states.remove(K));
    }

    public void setPoses(Poses pose) {
        this.pose = pose;
    }

    public Map<States, Byte> getStates() { return states; }

    public Poses getPose() { return pose; }

    public void setHandState(boolean value) {
        this.isHandTriggered = value;
    }

    public boolean getHandState() { return isHandTriggered; }

    @Override
    public MetaDataContainer clone() {
        try {
            MetaDataContainer clone = new MetaDataContainer();
            this.states.keySet().forEach(clone::setStates);
            clone.setPoses(this.pose);
            return clone;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
