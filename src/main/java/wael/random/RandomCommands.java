package wael.random;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import wael.random.Random;

public class RandomCommands {

    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {

            dispatcher.register(Commands.literal("start")
                    .executes(ctx -> {
                        MinecraftServer server = ctx.getSource().getServer();

                        if (Random.gameActive) {
                            ctx.getSource().sendSuccess(() ->
                                    Component.literal("It's already running, wake up")
                                            .withStyle(ChatFormatting.YELLOW), false);
                            return 0;
                        }

                        Random.gameActive = true;
                        Random.tickCounter = 0;
                        Random.craftingRandomized = true;
                        Random.dropsRandomized = true;

                        server.getPlayerList().broadcastSystemMessage(
                                Component.literal("A Worthy Challenger Has Come... HUH!")
                                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                                false);

                        return 1;
                    }));

            dispatcher.register(Commands.literal("stop")
                    .executes(ctx -> {
                        MinecraftServer server = ctx.getSource().getServer();

                        if (!Random.gameActive) {
                            ctx.getSource().sendSuccess(() ->
                                    Component.literal("It isn't running, wake up")
                                            .withStyle(ChatFormatting.YELLOW), false);
                            return 0;
                        }

                        Random.gameActive = false;
                        Random.tickCounter = 0;
                        Random.craftingRandomized = false;
                        Random.dropsRandomized = false;

                        server.getPlayerList().broadcastSystemMessage(
                                Component.literal("The Challenger has left...")
                                        .withStyle(ChatFormatting.GRAY),
                                false);

                        return 1;
                    }));
        });
    }
}