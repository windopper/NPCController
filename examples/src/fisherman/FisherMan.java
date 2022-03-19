package fisherman;

import com.kamilereon.npccontroller.NPCController;
import com.kamilereon.npccontroller.NPCManager;
import com.kamilereon.npccontroller.behavior.LookAtNearestPlayer;
import com.kamilereon.npccontroller.behavior.RandomLookAround;
import fisherman.behaviors.ProtectingMyFishes;
import fisherman.behaviors.StoreFishes;
import fisherman.behaviors.ThrowHook;
import fisherman.behaviors.CaughtFish;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FisherMan {
    public static void create(Location location, Player player) {
        NPCManager npcManager = NPCController.createNewNPC(location, "전영우", "x2NyPtU1Va/5uQ2rqqWpZ7lLDHDGNg8RvPTFKvtkibswdOQw/7McM79wMn31EkIB3DfR1vMlPVdBNC+uMdsWRepPK2zWQz4bzRwju7Z3WctRG+CB3ZmILcVTi+xFxXoWsmNKeF6clo5cODcwg2ObnGdGT0CCLu7L5duio94wRQKjDEef4yE62bzcLYngG/6IU06oOVdWlBUvYg9OZ6TnlAdRLl/woGRzKuwpYDAmxKa5Zm96V4RdtaEdkgg6dZrINiDPG/58OP5o53xLDt1h821DFC+ZqrCNfPC4l2swNhPI4F+6FYaLM+IUPBS0MzkryeUT3Ln7OJcUVsCL9lFIUUdhg46OQCWxVfdl9Yp4xz/Yue8WKfOfh/edj1snmmQKp85esDd0C/wMwVc0cnFAGwGxZgqV/xv3HQ4ff5FeoQVc18Xeg6pu9WwoNc9SXaUZL9rZ49MdnG1Kofl7GQ6zkwIq5YcUU+k3sbpV8GDw0gUQWKIa8BvbOju/BMCz32gfaFMkQ98YcETjIaXMFeYcMLgjtNVUCJjTVtjfxiTIG5MkVODHj5wZKIeUsB9Pr1Qa6wHRq4/Qsd/ta7jz0uUNm9gmlV7UJhNbDHD7Ql8su/UmG5LmfPxkIOqLVKxkdYkkdZdu6qsEa3kBcPR/cRX/LVDt0X3+M6XBJB6Ar7f6FMQ=", "ewogICJ0aW1lc3RhbXAiIDogMTYyNTY5MjkxMTI1MSwKICAicHJvZmlsZUlkIiA6ICIyMDA2NTVkMjMyYTE0MTc2OGIwNjQ0NWNkNTliNDg3NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaWVzdHlCbHVlXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NjQ2YWM0OTc2MTlkZWRmODU5Y2Q2ZGQxYTRkYWFiOTA1ODMzOGM0NWE4ZmNiODk3MmQ1NDRiNTk4ZjY3MjI4IgogICAgfQogIH0KfQ==");

        npcManager.setAI();
        npcManager.setCanPickUpItem(true);
        npcManager.showToAll();

        npcManager.setBehavior(0, new CaughtFish());
        npcManager.setBehavior(0, new ProtectingMyFishes(5));
        npcManager.setBehavior(1, new ThrowHook());
        npcManager.setBehavior(2, new StoreFishes(20));
        npcManager.setBehavior(3, new RandomLookAround());
        npcManager.setBehavior(4, new LookAtNearestPlayer(3, (p) -> true, 0.25));

    }
}
