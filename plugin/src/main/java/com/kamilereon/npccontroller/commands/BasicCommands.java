package com.kamilereon.npccontroller.commands;

import archer.Archer;
import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.standard.Idle;
import com.kamilereon.npccontroller.behavior.standard.LookAtNearestPlayer;
import com.kamilereon.npccontroller.behavior.standard.RandomLookAround;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import fisherman.FisherMan;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import trader.Trader;

public class BasicCommands {

    public static NPCManager npcManager;

    public static void listen(Player player, String[] args) {
        if(args.length == 1) {
            switch (args[0]) {
                case "default" -> {
                    npcManager = NPCController.createNewNPC(player.getLocation());
                    npcManager.setAI();
                    npcManager.showToAll();
                }
                case "player" -> {
                    player.sendMessage(Bukkit.getOnlinePlayers().size() + " count(s)");
                }
                case "lookAtMe" -> {
                    npcManager.lookAt(player.getEyeLocation());
                }
                case "here" -> {
                    EntityZombie ev = npcManager.getAI();
                    Location loc = player.getLocation();
//                    ev.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 1.8);
                    npcManager.navigateTo(loc, 1.8, 1);
                }
                case "jump" -> {
                    EntityZombie ev = npcManager.getAI();
                    Location loc = player.getLocation();
                    ev.getControllerJump().jump();
                }
                case "leap" -> {
                    npcManager.leap(0.5f);
                }
                case "fisherman" -> {
                    FisherMan.create(player.getLocation(), player);
                }
                case "archer" -> {
                    Archer.create(player.getLocation());
                }
                case "trader" -> {
                    Trader.createTrader(player.getLocation());
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
