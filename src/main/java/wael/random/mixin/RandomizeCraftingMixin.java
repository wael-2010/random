package wael.random.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wael.random.Random;

@Mixin(CraftingMenu.class)
public class RandomizeCraftingMixin {

    @Inject(method = "slotChangedCraftingGrid", at= @At("TAIL"))
    private static void onSlotChanged(AbstractContainerMenu menu, ServerLevel level, Player player,
                                      CraftingContainer craftSlots, ResultContainer resultSlots,
                                      RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci) {
        if (!Random.craftingRandomized) {
            return;
        }
        ItemStack current = resultSlots.getItem(0);

        if(current.isEmpty()) {
            return;
        }

        int randomID= level.getRandom().nextInt(BuiltInRegistries.ITEM.size());
        Item randomItem = BuiltInRegistries.ITEM.byId(randomID);

        resultSlots.setItem(0, new ItemStack(randomItem, current.getCount()));
    }


}
