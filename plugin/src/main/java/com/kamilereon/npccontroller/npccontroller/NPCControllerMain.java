package com.kamilereon.npccontroller.npccontroller;

import org.bukkit.plugin.java.JavaPlugin;

public final class NPCControllerMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NPCController enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
