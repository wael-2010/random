package wael.random;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class RandomBlockDrops {
    public static void registerBlockDrop() {
        LootTableEvents.MODIFY_DROPS.register(((holder, context, drops) -> {
            if (!Random.dropsRandomized) {
                return;
            }

            if (!context.hasParameter(LootContextParams.BLOCK_STATE)) {
                return;
            }

            if (drops.isEmpty()) {
                return;
            }

            ServerLevel level = context.getLevel();
            List<ItemStack> original = List.copyOf(drops);

            drops.clear();

            for (ItemStack stack : original) {
                int randomId = level.getRandom().nextInt(BuiltInRegistries.ITEM.size());

                Item randomItem = BuiltInRegistries.ITEM.byId(randomId);

                drops.add(new ItemStack(randomItem, stack.getCount()));
            }
        }));
    }


}
