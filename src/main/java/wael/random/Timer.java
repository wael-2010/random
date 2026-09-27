package wael.random;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class Timer {

    private static final int TICKS_SECONDS = 20;

    private static final int START_SECONDS = 15;

    private static int tickCounter = 0;
    private static int secondsLeft = START_SECONDS;


    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(Timer::onServerTick);
    }

    public static void resetTimer() {
        tickCounter = 0;
        secondsLeft = START_SECONDS;
    }

    private static void onServerTick(MinecraftServer server) {
        if (!Random.countdownTimer)
            return;

        if (tickCounter == 0) {
            showTimer(server, secondsLeft);

        }
        tickCounter++;

        if(tickCounter < TICKS_SECONDS)
            return;
        tickCounter = 0;

        secondsLeft--;

        if (secondsLeft < 0) {
            secondsLeft = START_SECONDS;

        }

        showTimer(server, secondsLeft);
    }


    private static void showTimer(MinecraftServer server, int seconds) {
        Component text = Component.literal(String.valueOf(seconds)).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
        ClientboundSetActionBarTextPacket packet = new ClientboundSetActionBarTextPacket(text);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(packet);
        }
    }
}