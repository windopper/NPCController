package archer.behaviors;

import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.Behavior;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.states.Poses;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import net.minecraft.world.entity.monster.EntitySkeleton;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.im.InputContext;

public class BowShoot extends Behavior {

    protected Entity target;
    protected int tick;
    protected int mtick;
    protected boolean sneak = false;

    public BowShoot(NPCManager npcManager) {
        super(npcManager);
    }

    @Override
    public boolean check() {
        MemoryModule<?> module = npcManager.getMemoryModuleIfPresent("target");
        if(module == null ) return false;
        Entity t = (Entity) module.getData();
        if(t == null) return false;
        this.target = t;
        return true;
    }

    @Override
    public boolean whileCheck() {
        return this.tick > 0;
    }

    @Override
    public void firstAct() {
        this.tick = npcManager.getRandom().nextInt(5)+25;
        this.mtick = this.tick;
        npcManager.triggerHand(true);
        npcManager.lookAt(target.getLocation().add(0, 2.3, 0));
    }

    @Override
    public void endAct() {
        npcManager.triggerHand(false);
        b();
        this.tick = 0;
        this.target = null;
        this.mtick = 0;
    }

    @Override
    public void act() {
        --this.tick;

        npcManager.lookAt(target.getLocation().add(0, 1.6, 0));

        if(this.tick == 10) {
            Arrow arrow = npcManager.getNPC().getBukkitEntity().launchProjectile(Arrow.class);
            npcManager.triggerHand(false);
        }
        if(this.tick == 9) {
            b();

            if(npcManager.getAI().isOnGround()) {
                if(npcManager.getRandom().nextDouble() > 0.2) {
                    Vector v = npcManager.getLocation().getDirection();
                    v.normalize().multiply(0.2);
                    v.setY(0);
                    if(npcManager.getRandom().nextDouble() > 0.5) v.rotateAroundY(Math.toRadians(90));
                    else v.rotateAroundY(Math.toRadians(-90));
                    if(npcManager.getRandom().nextDouble() > 0.6) v.setY(0.4);
                    npcManager.getAI().getBukkitEntity().setVelocity(v);
                }
            }
        }
        if(this.mtick - 5 < this.tick) {
            a(0.1);
        }
    }

    private void a(double rate) {
        if(npcManager.getRandom().nextDouble() > rate || this.sneak) return;
        this.sneak = true;
        npcManager.setPoses(Poses.SNEAKING);
    }

    private void b() {
        if(this.sneak) {
            npcManager.setPoses(Poses.STANDING);
            this.sneak = false;
        }
    }
}
