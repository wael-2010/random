package wael.random;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Random implements ModInitializer {
	public static final String MOD_ID = "random";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean gameActive = false;
	public static int tickCounter = 0;
	public static boolean craftingRandomized = false;

	public static boolean dropsRandomized = false;
	public static boolean armorRandomized = false;
	public static boolean inventoryShuffled = false;
	public static boolean chunkSwap = false;



	@Override
	public void onInitialize() {
		RandomCommands.registerCommand();

		RandomBlockDrops.registerBlockDrop();

		RandomArmorSlots.register();
		InventoryShuffle.register();
		RandomChunk.register();



	}
}
