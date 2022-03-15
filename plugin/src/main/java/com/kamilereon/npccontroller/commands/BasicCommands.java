package com.kamilereon.npccontroller.commands;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.NPCControllerMain;
import com.kamilereon.npccontroller.states.ItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BasicCommands {
    public static void listen(Player player, String[] args) {
        if(args.length == 1) {
            switch (args[0]) {
                case "create" -> {
                    NPCManager npcManager = NPCController.createNewNPC(player.getLocation());
                    npcManager.showTo(player);
                    npcManager.lookAt(player.getEyeLocation());
                    npcManager.setEquipment(ItemSlot.CHEST, new ItemStack(Material.DIAMOND_CHESTPLATE));


                    Bukkit.getScheduler().scheduleSyncDelayedTask(NPCControllerMain.getPlugin(NPCControllerMain.class),
                            () -> npcManager.lookAt(player.getEyeLocation()), 40);
                }
                case "player" -> {
                    player.sendMessage(Bukkit.getOnlinePlayers().size()+" count(s)");
                }
            }

        }
    }
}
