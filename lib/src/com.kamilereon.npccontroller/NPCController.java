package com.kamilereon.npccontroller;

import com.kamilereon.npccontroller.utils.Logger;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class NPCController {

    public static final Plugin plugin = NPCControllerMain.getPlugin(NPCControllerMain.class);
    private static final Set<NPCManager> NPC_MANAGERS = new ConcurrentSet<>();
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

    public static NPCManager createNewNPC(Location location) {
        NPCController npcController = getInstance();
        try {
            NPCManager npcManager = (NPCManager) npcController.nmsClass.getConstructor().newInstance();
            npcManager.create(location);
            NPC_MANAGERS.add(npcManager);
            return npcManager;
        }
        catch(Exception e) {
            npcController.logger.severe("Error while creating new npc");
        }
        return null;
    }

    public static NPCManager createNewNPC(Location location, String signature, String texture) {
        NPCController npcController = getInstance();
        try {
            NPCManager npcManager = (NPCManager) npcController.nmsClass.getConstructor().newInstance();
            npcManager.setSkin(signature, texture);
            npcManager.create(location);
            NPC_MANAGERS.add(npcManager);
            return npcManager;
        }
        catch(Exception e) {
            npcController.logger.severe("Error while creating new npc");
        }
        return null;
    }

    public static NPCManager createNewNPC(Location location, String name, String signature, String texture) {
        NPCController npcController = getInstance();
        try {
            NPCManager npcManager = (NPCManager) npcController.nmsClass.getConstructor().newInstance();
            npcManager.create(location, name, signature, texture);
            NPC_MANAGERS.add(npcManager);
            return npcManager;
        }
        catch(Exception e) {
            npcController.logger.severe("Error while creating new npc");
        }
        return null;
    }

    public static void removeNPCManager(NPCManager npcManager) {
        NPC_MANAGERS.remove(npcManager);
    }

    public static PlayerConnection getConnection(Player player) {
        NPCController npcController = getInstance();
        try {
            NPCManager npcManager = (NPCManager) npcController.nmsClass.getConstructor().newInstance();
            return npcManager.getPlayerConnection(player);
        }
        catch(Exception e) {
            npcController.logger.severe("Error while getting connection");
        }
        return null;
    }

    public Logger getLogger() { return logger; }

    public static Set<NPCManager> getNpcManagers() { return NPC_MANAGERS; }

    public static NPCManager getNPCManager(int id) {
        for(NPCManager npcManager : getNpcManagers()) {
            if(npcManager.getNPC().getId() == id) {
                return npcManager;
            }
        }
        return null;
    }
}
