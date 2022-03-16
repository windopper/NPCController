package com.kamilereon.npccontroller.commands;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.NPCControllerMain;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.behavior.Idle;
import com.kamilereon.npccontroller.behavior.LookAtNearestPlayer;
import com.kamilereon.npccontroller.metadata.BehaviorContainer;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import net.minecraft.world.entity.npc.EntityVillager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftVillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public class BasicCommands {

    public static NPCManager npcManager;

    public static void listen(Player player, String[] args) {
        if(args.length == 1) {
            switch (args[0]) {
                case "create" -> {
                    npcManager = NPCController.createNewNPC(player.getLocation());
                    npcManager.showTo(player);
                    npcManager.setEquipment(ItemSlot.CHEST, new ItemStack(Material.DIAMOND_CHESTPLATE));
                    npcManager.setVillagerAI();
//                    npcManager.setBehavior(1, new LookAtNearestPlayer(4, (p) -> true));
//                    npcManager.setBehavior(0, new Idle(40, 20, player.getLocation()));
                }
                case "player" -> {
                    player.sendMessage(Bukkit.getOnlinePlayers().size() + " count(s)");
                }
                case "lookAtMe" -> {
                    npcManager.lookAt(player.getEyeLocation());
                }
                case "here" -> {
                    Villager villager = npcManager.getAI();
                    EntityVillager ev = ((CraftVillager) villager).getHandle();
                    ev.getNavigation().a(((CraftPlayer) player).getHandle(), 5);
                }
            }
        }
        else if(args.length == 2) {
            switch(args[0]) {
                case "animation" -> {
                    npcManager.playAnimation(Animation.valueOf(args[1]));
                }
                case "pose" -> {
                    npcManager.setPoses(Poses.valueOf(args[1]));
                }
                case "state" -> {
                    npcManager.setStates(States.valueOf(args[1]));
                }
            }
        }
        else if(args.length == 3) {
            switch(args[0]) {
                case "equips" -> {
                    npcManager.setEquipment(ItemSlot.valueOf(args[1]), new ItemStack(Material.valueOf(args[2]), 1));
                }
            }
        }
    }

}
