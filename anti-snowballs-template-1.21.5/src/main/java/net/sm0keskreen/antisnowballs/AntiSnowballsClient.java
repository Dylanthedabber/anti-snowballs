package net.sm0keskreen.antisnowballs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class AntiSnowballsClient implements ClientModInitializer {
    private boolean enabled = true;
    private int lastDeletedCount = 0;
    private int totalDeletedCount = 0; // New counter

    @Override
    public void onInitializeClient() {
        // Register commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("antisnowballs")
                    .then(ClientCommandManager.literal("toggle")
                            .executes(context -> {
                                enabled = !enabled;
                                context.getSource().sendFeedback(Text.literal("AntiSnowballs is now " + (enabled ? "enabled" : "disabled")));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("count")
                            .executes(context -> {
                                context.getSource().sendFeedback(Text.literal("Snowballs deleted last tick: " + lastDeletedCount));
                                context.getSource().sendFeedback(Text.literal("Total snowballs deleted: " + totalDeletedCount));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("reset")
                            .executes(context -> {
                                totalDeletedCount = 0;
                                context.getSource().sendFeedback(Text.literal("Snowball deletion counter reset."));
                                return 1;
                            })
                    )
            );
        });

        // Tick event to remove snowballs
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;
            if (!enabled) {
                lastDeletedCount = 0;
                return;
            }

            var player = client.player;
            Box box = new Box(
                    player.getX() - 100, player.getY() - 100, player.getZ() - 100,
                    player.getX() + 100, player.getY() + 100, player.getZ() + 100
            );

            var snowballs = client.world.getEntitiesByClass(SnowballEntity.class, box, e -> true);
            lastDeletedCount = snowballs.size();
            totalDeletedCount += lastDeletedCount; // Accumulate total

            snowballs.forEach(s -> s.remove(RemovalReason.DISCARDED));
        });
    }
}
