package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.NPC;

public abstract class Behavior {

    protected boolean canForceStop = true;
    protected boolean forceStartIfNecessary = false;
    protected NPCManager npcManager;

    public Behavior(NPCManager npcManager) {
        this.npcManager = npcManager;
    }

    public abstract boolean check();
    public boolean whileCheck() { return false; };
    public void firstAct() {}
    public void endAct() {}
    public abstract void act();

    public boolean isCanForceStop() { return this.canForceStop; }
    public boolean isCanForceStart() { return this.forceStartIfNecessary; }
}
