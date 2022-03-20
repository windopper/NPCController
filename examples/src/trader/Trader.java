package trader;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.standard.Idle;
import com.kamilereon.npccontroller.behavior.standard.RandomLookAround;
import com.kamilereon.npccontroller.behavior.standard.WalkToDestination;
import com.kamilereon.npccontroller.memory.MemoryModule;
import com.kamilereon.npccontroller.memory.MemoryType;
import org.bukkit.Location;
import trader.behaviors.AskingTrade;
import trader.behaviors.FindTraders;

public class Trader {
    public static void createTrader(Location location) {
        NPCManager npcManager = NPCController.createNewNPC(location, "Trader", "", "");
        npcManager.setAI();
        npcManager.setCanPickUpItem(true);
        npcManager.showToAll();

        npcManager.putMemoryModule(new MemoryModule<>(MemoryType.LONG_TERM, "Trader", null));

        npcManager.setBehavior(0, new WalkToDestination(npcManager, 2));
        npcManager.setBehavior(1, new FindTraders(npcManager));
        npcManager.setBehavior(2, new AskingTrade(npcManager));
        npcManager.setBehavior(10, new Idle(npcManager, 1.0, 0.75));
        npcManager.setBehavior(11, new RandomLookAround(npcManager));
    }
}
