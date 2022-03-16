package com.kamilereon.npccontroller.utils;

import com.kamilereon.npccontroller.NPCController;
import org.bukkit.Bukkit;

public class SchedulerUtils {
    public static void runAfter(Runnable runnable, int tick) {
        Bukkit.getScheduler().runTaskLater(NPCController.plugin, runnable, tick);
    }
}
