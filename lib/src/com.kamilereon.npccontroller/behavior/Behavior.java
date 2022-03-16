package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.server.level.EntityPlayer;

public abstract class Behavior {
    public abstract boolean check(NPCManager npcManager);
    public abstract void act(NPCManager npcManager);
}
