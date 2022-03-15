package com.kamilereon.npccontroller.npccontroller.commands;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import org.bukkit.entity.Player;

public class BasicCommands {
    public static void listen(Player player, String[] args) {
        if(args.length == 1) {
            if(args[0].equals("create")) {
                NPCManager npcManager = NPCController.createNewNPC(player.getLocation());
                npcManager.showTo(player);
            }
        }
    }
}
