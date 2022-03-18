package com.kamilereon.npccontroller.commands;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.*;
import com.kamilereon.npccontroller.states.Animation;
import com.kamilereon.npccontroller.states.ItemSlot;
import com.kamilereon.npccontroller.states.Poses;
import com.kamilereon.npccontroller.states.States;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BasicCommands {

    public static NPCManager npcManager;

    public static void listen(Player player, String[] args) {
        if(args.length == 1) {
            switch (args[0]) {
                case "create" -> {
                    npcManager = NPCController.createNewNPC(player.getLocation(), "1atte", "", "");
                    npcManager.setAI();
                    npcManager.showTo(player);
//                    npcManager.setSkin("N0/34BBcnfe8x2gF8xwdhQ1fpFuU5bT1y3/uh0NsSja0UwTblI0qK0UzF7EPpye+O+ZrbVAp82DuDioC6LH/Al0dQRqUFRETgQuJRSJRNavpgikDCKE7TRqFclMELvxQ5xika0HpoR6+bI80H82+9H+4ePrhL8W9JVacCDiq9m8/TEG9SlUKsHxbg0cjXKi7xfOouk6LvIZl68PtdZlkVCmOzgTDZgX3fJ6lXjl0gSmu+afLZ7bKumoKBFWYddacwlBLIqnuxHK+byd9wb5Kg45Lle0CH2edcNxVydcPgEG9wSwf8aHJbryQQFtJMjRooZQgGBn/aFFM7hpo+CuG7w2B2kZ6YPMyTzRhJoEvJDdyeweAPssyTqTkLn32/cJ2Mot18PJHJSnekp/CFJaqIKRbGkNBkYZIzuy/IuC5noAftI41J4Ty3IumEIeLyRRD4w2Bh68pIBSwOe5rxmGrkF4USfumdejUtHo4C6AxhQ/N9kbvv6Yn/Z8+wX7srIhDuqBtYBv/31q30G/cGI0aq3MIFR2dueTNO3Oj2+4XVlp7Dpz5g2K5Cg0UhS8xdHsj38SjLJA+TWaT9fnXAkBUnNLoQ8McXUOe9WKwqleDmSszQiMBR66t3zWE17XeGvOznIMYmBfW//GN1VYQPNjUyGr3T2vPgmQF8AMV72YlLYQ=",
//                            "ewogICJ0aW1lc3RhbXAiIDogMTYxNzk1MTkyNzUzOCwKICAicHJvZmlsZUlkIiA6ICI3MzlkYzg4OThmODg0OTRmOGNkNDE4NDI4NjUxNzBkYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJlZGlzb24xMzA0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM4MjYwNjkzNjQwYzIxYWJjZDNiYTQwYjI4N2ZmMzhiMGIxNGMyYTljM2FlMmFlOGVhYmIwZTNiNDU3YzJiMmUiCiAgICB9CiAgfQp9");
//                    npcManager.setBehavior(0, new FarmingWheat(10));
                    npcManager.setBehavior(0, new ChickenFriedDone());
                    npcManager.setBehavior(1, new ChickenFried(10));
                    npcManager.setBehavior(2, new Idle(1, 0.25));
                    npcManager.setBehavior(3, new RandomLookAround());
                    npcManager.setBehavior(4, new LookAtNearestPlayer(6, (p) -> true, 0.25));
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
