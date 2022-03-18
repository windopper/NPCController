package com.kamilereon.npccontroller.behavior;

import com.kamilereon.npccontroller.AIEntity;
import com.kamilereon.npccontroller.NPCManager;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.level.block.BlockDoor;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.pathfinder.PathPoint;
import org.bukkit.Bukkit;

public class InteractBlockIfPossible extends Behavior{

    protected BlockPosition blockPosition;
    protected AIEntity ai;
    protected boolean validBlock = false;
    protected boolean a;
    private float b;
    private float c;
    private int tick;

    @Override
    public boolean check(NPCManager npcManager) {
        this.ai = npcManager.getAI();
        Navigation nav = (Navigation) this.ai.getNavigation();
        PathEntity pe = nav.k();
        if(pe != null && !pe.c() && nav.f()) {
            for(int i =0; i<Math.min(pe.f() + 2, pe.e()); ++i) {
                PathPoint pp = pe.a(i);
                blockPosition = new BlockPosition(pp.a, pp.b+1, pp.c);
                if(!(this.ai.h((double)blockPosition.getX(), ai.locY(), (double)blockPosition.getZ()) > 2.25D)) {
                    this.validBlock = BlockDoor.a(this.ai.t, this.blockPosition);
                    if(this.validBlock) {
                        return true;
                    }
                }
            }

            this.blockPosition = this.ai.getChunkCoordinates().up();
            this.validBlock = BlockDoor.a(this.ai.t, this.blockPosition);
            return this.validBlock;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean whileCheck(NPCManager npcManager) {
        return this.a && this.tick > 0;
    }

    @Override
    public void firstAct(NPCManager npcManager) {
        this.tick = 20;
        this.a = false;
        this.b = (float)((double)this.blockPosition.getX() + 0.5D - this.ai.locX());
        this.c = (float)((double) this.blockPosition.getZ() + 0.5D - this.ai.locZ());

        this.doorinteract(true);
        Bukkit.broadcastMessage("door");
    }

    @Override
    public void endAct(NPCManager npcManager) {
        this.doorinteract(false);
    }

    @Override
    public void act(NPCManager npcManager) {
        --this.tick;
        float var0 = (float)((double)this.blockPosition.getX() + 0.5D - this.ai.locX());
        float var1 = (float)((double)this.blockPosition.getZ() + 0.5D - this.ai.locZ());
        float var2 = this.b * var0 + this.c * var1;
        if(var2 < 0.0F) { this.a = true; }
    }

    public void doorinteract(boolean value) {
        if(this.a) {
            IBlockData var1 = this.ai.t.getType(this.blockPosition);
            if(var1.getBlock() instanceof BlockDoor blockDoor) {
                blockDoor.setDoor(this.ai, this.ai.t, var1, this.blockPosition, value);
            }
        }
    }
}
