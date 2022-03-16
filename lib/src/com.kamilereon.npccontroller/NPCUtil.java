package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.metadata.BehaviorContainer;

public interface NPCUtil {
    void setBehavior(int priority, Behavior behavior);
//    void setBehavior(int priority, BehaviorContainer.EnumBehavior behavior);
}
