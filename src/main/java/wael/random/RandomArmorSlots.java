package wael.random;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RandomArmorSlots {

    private static final int TICKS = 20;

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private static int counter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(RandomArmorSlots::onTick);
    }
    private static void onTick(MinecraftServer server) {
        if (!Random.armorRandomized) {
            return;
        }
    counter++;
        if (counter < TICKS) {
            return;
        }
        counter = 0;
        for (ServerPlayer player: server.getPlayerList().getPlayers()) {
            for (EquipmentSlot slot : ARMOR_SLOTS) {
                int randomId = player.getRandom().nextInt(BuiltInRegistries.ITEM.size());

                Item randomItem = BuiltInRegistries.ITEM.byId(randomId);
                player.setItemSlot(slot, new ItemStack(randomItem,1));
            }
        }
    }
}