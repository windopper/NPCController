package com.kamilereon.npccontroller.commands;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.*;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class BasicCommands {

    public static NPCManager npcManager;

    public static void listen(Player player, String[] args) {
        if(args.length == 1) {
            switch (args[0]) {
                case "create" -> {
                    npcManager = NPCController.createNewNPC(player.getLocation());
                    npcManager.setEquipment(ItemSlot.CHEST, new ItemStack(Material.DIAMOND_CHESTPLATE));
                    npcManager.showTo(player);
                    npcManager.setAI();
                    npcManager.setSkin("N0/34BBcnfe8x2gF8xwdhQ1fpFuU5bT1y3/uh0NsSja0UwTblI0qK0UzF7EPpye+O+ZrbVAp82DuDioC6LH/Al0dQRqUFRETgQuJRSJRNavpgikDCKE7TRqFclMELvxQ5xika0HpoR6+bI80H82+9H+4ePrhL8W9JVacCDiq9m8/TEG9SlUKsHxbg0cjXKi7xfOouk6LvIZl68PtdZlkVCmOzgTDZgX3fJ6lXjl0gSmu+afLZ7bKumoKBFWYddacwlBLIqnuxHK+byd9wb5Kg45Lle0CH2edcNxVydcPgEG9wSwf8aHJbryQQFtJMjRooZQgGBn/aFFM7hpo+CuG7w2B2kZ6YPMyTzRhJoEvJDdyeweAPssyTqTkLn32/cJ2Mot18PJHJSnekp/CFJaqIKRbGkNBkYZIzuy/IuC5noAftI41J4Ty3IumEIeLyRRD4w2Bh68pIBSwOe5rxmGrkF4USfumdejUtHo4C6AxhQ/N9kbvv6Yn/Z8+wX7srIhDuqBtYBv/31q30G/cGI0aq3MIFR2dueTNO3Oj2+4XVlp7Dpz5g2K5Cg0UhS8xdHsj38SjLJA+TWaT9fnXAkBUnNLoQ8McXUOe9WKwqleDmSszQiMBR66t3zWE17XeGvOznIMYmBfW//GN1VYQPNjUyGr3T2vPgmQF8AMV72YlLYQ=",
                            "ewogICJ0aW1lc3RhbXAiIDogMTYxNzk1MTkyNzUzOCwKICAicHJvZmlsZUlkIiA6ICI3MzlkYzg4OThmODg0OTRmOGNkNDE4NDI4NjUxNzBkYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJlZGlzb24xMzA0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM4MjYwNjkzNjQwYzIxYWJjZDNiYTQwYjI4N2ZmMzhiMGIxNGMyYTljM2FlMmFlOGVhYmIwZTNiNDU3YzJiMmUiCiAgICB9CiAgfQp9");

                    npcManager.addDropTable(new ItemStack(Material.STONE, 64), 1);
                    npcManager.setBehavior(0, new Assasination(10));
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
//                    ev.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 1.8);
                    npcManager.navigateTo(loc, 1.8);
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
