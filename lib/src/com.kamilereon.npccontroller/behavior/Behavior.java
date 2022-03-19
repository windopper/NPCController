package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.server.level.EntityPlayer;

public abstract class Behavior {

    protected boolean canForceStop = true;
    protected boolean forceStartIfNecessary = false;

    public abstract boolean check(NPCManager npcManager);
    public boolean whileCheck(NPCManager npcManager) { return false; };
    public void firstAct(NPCManager npcManager) {}
    public void endAct(NPCManager npcManager) {}
    public abstract void act(NPCManager npcManager);

    public boolean isCanForceStop() { return this.canForceStop; }
    public boolean isCanForceStart() { return this.forceStartIfNecessary; }
}
