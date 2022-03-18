package com.kamilereon.npccontroller.listener;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import io.netty.channel.*;
import net.minecraft.network.protocol.game.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntListIterator;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;

public class PacketListener implements Listener {

    private String pre = "";

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        injectPlayer(player);
    }
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removePlayer(player);
    }
    private void removePlayer(Player player) {
        Channel channel = NPCController.getConnection(player).a.k;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName()+"_NCT");
            return null;
        });
    }

    private void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                if(packet instanceof PacketPlayInUseEntity packetPlayInUseEntity) {

                }
                super.channelRead(ctx, packet);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
                super.write(ctx, packet, promise);
                if(packet instanceof PacketPlayOutEntityDestroy packetPlayOutEntityDestroy) {
                    IntList intList = (IntList) getValue(packetPlayOutEntityDestroy, "a");
                    for(NPCManager npcManager : NPCController.getNpcManagers()) {
                        int entityId = npcManager.getNPC().getId();
                        for(int id : intList) {
                            if(id == entityId && !npcManager.isDestroyed()) {
                                Bukkit.broadcastMessage("destroyed!");
                                npcManager.addFarShowns(player);
                            }
                        }
                    }
                }
//                if(packet instanceof PacketPlayOutChat) return;
//                if(packet instanceof PacketPlayOutAbilities) return;
//                if(packet instanceof PacketPlayOutUpdateAttributes) return;
//                if(packet instanceof PacketPlayInFlying) return;
//                if(packet instanceof PacketPlayOutLightUpdate) return;
//                if(packet instanceof PacketPlayOutMapChunk) return;
//
//                if(!pre.equals(packet.getClass().getName())) {
//                    pre = packet.getClass().getName();
//                }
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().b.a.k.pipeline();
        pipeline.addBefore("packet_handler", player.getName()+"_NCT", channelDuplexHandler);
    }

    private Object getValue(Object object, String fieldName) {
        Object result = null;
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            result = field.get(object);
            field.setAccessible(false);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
