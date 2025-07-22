package com.antisnowballs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class AntiSnowballsClient implements ClientModInitializer {
    private boolean enabled = true;
    private int lastDeletedCount = 0;
    private int totalDeletedCount = 0;

    @Override
    public void onInitializeClient() {
        // Register /antisnowballs commands: toggle, count, reset
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("antisnowballs")
                    .then(ClientCommandManager.literal("toggle").executes(ctx -> {
                        enabled = !enabled;
                        ctx.getSource().sendFeedback(
                                Text.literal("AntiSnowballs is now " + (enabled ? "enabled" : "disabled")));
                        return 1;
                    }))
                    .then(ClientCommandManager.literal("count").executes(ctx -> {
                        ctx.getSource().sendFeedback(
                                Text.literal("Snowballs deleted last tick: " + lastDeletedCount));
                        ctx.getSource().sendFeedback(
                                Text.literal("Total snowballs deleted: " + totalDeletedCount));
                        return 1;
                    }))
                    .then(ClientCommandManager.literal("reset").executes(ctx -> {
                        totalDeletedCount = 0;
                        ctx.getSource().sendFeedback(
                                Text.literal("Snowball deletion counter reset."));
                        return 1;
                    }))
            );
        });

        // Hook into client tick events to remove nearby snowballs
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MinecraftClient mc = client;
            if (mc.world == null || mc.player == null) {
                lastDeletedCount = 0;
                return;
            }
            if (!enabled) {
                lastDeletedCount = 0;
                return;
            }

            var player = mc.player;
            Box box = new Box(
                    player.getX() - 100, player.getY() - 100, player.getZ() - 100,
                    player.getX() + 100, player.getY() + 100, player.getZ() + 100
            );

            var snowballs = mc.world.getEntitiesByClass(
                    SnowballEntity.class, box, e -> true);
            lastDeletedCount = snowballs.size();
            totalDeletedCount += lastDeletedCount;

            snowballs.forEach(s -> s.remove(RemovalReason.DISCARDED));
        });
    }
}
