package com.kamilereon.npccontroller;

import net.minecraft.server.network.PlayerConnection;
import org.bukkit.entity.Player;

public interface PacketUtil {
    void fixSkinHelmetLayer();
    PlayerConnection getPlayerConnection(Player player);
}
