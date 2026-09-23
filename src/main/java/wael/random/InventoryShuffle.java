package wael.random;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.NonNullList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryShuffle {

    private static final int TICKS = 60;

    private static final int INVENTORY_SIZE = 36;

    private static int counter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(InventoryShuffle::onTick);
    }

    private static void onTick(MinecraftServer server) {
        if (!Random.inventoryShuffled) {
            return;
        }
        counter++;

        if (counter < TICKS) {
            return;
        }
        counter = 0;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Inventory inventory = player.getInventory();

            List<ItemStack> current = new ArrayList<>();

            for (int i = 0; i < INVENTORY_SIZE; i++) {
                current.add(inventory.getItem(i));
            }
            List<ItemStack> shuffled = new ArrayList<>(current);

            Collections.shuffle(shuffled);

            for (int i = 0; i < INVENTORY_SIZE; i++) {
                inventory.setItem(i, shuffled.get(i));
            }

            inventory.setChanged();
            player.containerMenu.broadcastChanges();
        }
    }
}
