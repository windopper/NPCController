package com.kamilereon.npccontroller.npccontroller;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.npccontroller.commands.BasicCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class NPCControllerMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NPCController enabled");
        NPCController.getInstance();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String commandName = command.getName();
        if(!commandName.equals("nct")) return true;
        BasicCommands.listen(player, args);
        return true;
    }
}
