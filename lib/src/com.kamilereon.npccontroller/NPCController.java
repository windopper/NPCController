package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.NPC;
import org.bukkit.plugin.java.JavaPlugin;

public class NPCController {

    private static final NPCController instance = new NPCController();
    private final Class<?> nmsClass;
    private final Logger logger;

    private NPCController() {
        this.logger = new Logger("NPCController");

        String versionName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName("com.kamilereon.npccontroller."+versionName+".NPCManager_"+versionName);
        }
        catch(Exception e) {

        }

        this.nmsClass = nmsClass;
        if(nmsClass == null) {
            logger.severe("version "+versionName+" is not supporting");
            return;
        }
    }

    public static NPCController getInstance() {
        return instance;
    }
}
