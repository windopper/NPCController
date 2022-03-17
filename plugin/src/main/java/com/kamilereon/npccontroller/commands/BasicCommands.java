package com.kamilereon.npccontroller.commands;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Greeting;
import com.kamilereon.npccontroller.behavior.Idle;
import com.kamilereon.npccontroller.behavior.LookAtNearestPlayer;
import com.kamilereon.npccontroller.behavior.RandomLookAround;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.pathfinder.PathEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BasicCommands {

    public static NPCManager npcManager;

    public static void listen(Player player, String[] args) {
        if(args.length == 1) {
            switch (args[0]) {
                case "create" -> {
                    npcManager = NPCController.createNewNPC(player.getLocation());
                    npcManager.showTo(player);
                    npcManager.setEquipment(ItemSlot.CHEST, new ItemStack(Material.DIAMOND_CHESTPLATE));
                    npcManager.setAI();
                    npcManager.setBehavior(0, new Greeting(1, (p) -> true));
                    npcManager.setBehavior(1, new Idle(1, 0.25));
                    npcManager.setBehavior(2, new RandomLookAround());
                    npcManager.setBehavior(3, new LookAtNearestPlayer(6, (p) -> true, 0.25));
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
                    ev.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 1.8);
                }

                case "jump" -> {
                    EntityZombie ev = npcManager.getAI();
                    Location loc = player.getLocation();
                    ev.getControllerJump().jump();
                }

                case "sit" -> {
                    npcManager.sit();
                }

                case "unsit" -> {
                    npcManager.unSit();
                }

                case "hit" -> {
                    EntityZombie ev = npcManager.getAI();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location loc = player.getLocation();
                            PathEntity pathEntity = ev.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 4);
                            ev.getNavigation().a(pathEntity, 1.6);
                            double dist = ev.getBukkitEntity().getLocation().distance(loc);
                            if(dist < 10) {
                                ev.getControllerLook().a(loc.getX(), loc.getY(), loc.getZ());
                            }
                            if(dist < 3.5) {
                                npcManager.playAnimation(Animation.SWING_MAIN_ARM);
                                ev.attackEntity(((CraftPlayer) player).getHandle());
                                cancel();
                            }
                        }
                    }.runTaskTimer(NPCController.plugin, 0, 1);
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
