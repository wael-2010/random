package wael.random;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class RandomChunk {

    private static final int INTERVAL_TICKS = 15 * 20;
    private static final int MIN_DISTANCE = 50;
    private static final int MAX_DISTANCE = 500;

    private static int tickCounter = 0;
    private static final Random RNG = new Random();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(RandomChunk::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        if (!wael.random.Random.chunkSwap) return;

        tickCounter++;
        if (tickCounter < INTERVAL_TICKS) return;
        tickCounter = 0;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerLevel level = (ServerLevel) player.level();
            BlockPos pos = player.blockPosition();

            int chunkAX = pos.getX() >> 4;
            int chunkAZ = pos.getZ() >> 4;

            int chunkBX = chunkAX + randomOffset();
            int chunkBZ = chunkAZ + randomOffset();

            swapChunks(level, chunkAX, chunkAZ, chunkBX, chunkBZ);
        }
    }

    private static int randomOffset() {
        int distance = MIN_DISTANCE + RNG.nextInt(MAX_DISTANCE - MIN_DISTANCE);
        return RNG.nextBoolean() ? distance : -distance;
    }

    private static void swapChunks(ServerLevel level, int chunkAX, int chunkAZ, int chunkBX, int chunkBZ) {
        level.getChunk(chunkAX, chunkAZ);
        level.getChunk(chunkBX, chunkBZ);

        HolderLookup.Provider registries = level.registryAccess();
        int minY = level.getMinY();
        int maxY = level.getMaxY();

        int baseAX = chunkAX * 16;
        int baseAZ = chunkAZ * 16;
        int baseBX = chunkBX * 16;
        int baseBZ = chunkBZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y <= maxY; y++) {
                    BlockPos posA = new BlockPos(baseAX + x, y, baseAZ + z);
                    BlockPos posB = new BlockPos(baseBX + x, y, baseBZ + z);

                    BlockState stateA = level.getBlockState(posA);
                    BlockState stateB = level.getBlockState(posB);

                    CompoundTag tagA = getSavedBlockEntity(level, posA, registries);
                    CompoundTag tagB = getSavedBlockEntity(level, posB, registries);

                    level.setBlock(posA, stateB, 3);
                    level.setBlock(posB, stateA, 3);

                    if (tagB != null) {
                        BlockEntity rebuilt = BlockEntity.loadStatic(posA, stateB, tagB, registries);
                        if (rebuilt != null) {
                            level.setBlockEntity(rebuilt);
                        }
                    }

                    if (tagA != null) {
                        BlockEntity rebuilt = BlockEntity.loadStatic(posB, stateA, tagA, registries);
                        if (rebuilt != null) {
                            level.setBlockEntity(rebuilt);
                        }
                    }
                }
            }
        }
    }

    private static CompoundTag getSavedBlockEntity(ServerLevel level, BlockPos pos, HolderLookup.Provider registries) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null ? be.saveWithFullMetadata(registries) : null;
    }
}