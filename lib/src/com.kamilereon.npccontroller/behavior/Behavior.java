package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.server.level.EntityPlayer;

public abstract class Behavior {
    public abstract boolean check(NPCManager npcManager);
    public boolean whileCheck(NPCManager npcManager) { return false; };
    public void firstAct(NPCManager npcManager) {}
    public void endAct(NPCManager npcManager) {}
    public abstract void act(NPCManager npcManager);
}
