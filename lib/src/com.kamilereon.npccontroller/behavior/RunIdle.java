package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.NPCManager;

public class RunIdle extends Idle{
    public RunIdle(double speed, double rate) {
        super(speed, rate);
    }

    @Override
    public void act(NPCManager npcManager) {
        if(npcManager.getAI().isOnGround()) {
            npcManager.leap(0.3f);
        }
//        npcManager.jump();
    }
}
