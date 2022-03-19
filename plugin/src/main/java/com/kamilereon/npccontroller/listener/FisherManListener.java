package com.kamilereon.npccontroller.listener;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FisherManListener implements Listener {
    @EventHandler
    public void PlayerFishEvent(PlayerFishEvent event) {
        PlayerFishEvent.State state = event.getState();
        Player player = event.getPlayer();
        int id = player.getEntityId();
        NPCManager npcManager = NPCController.getNPCManager(id);

        if(npcManager != null) {
            if(state == PlayerFishEvent.State.BITE) {
                npcManager.putMemoryModule(new MemoryModule<>(MemoryType.WORKING,"caught_fish", true));
            }
            else if(state == PlayerFishEvent.State.FAILED_ATTEMPT) {
                npcManager.removeMemoryModule("caught_fish");
            }
        }
    }
}
