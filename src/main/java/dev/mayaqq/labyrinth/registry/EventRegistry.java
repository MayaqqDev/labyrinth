package dev.mayaqq.labyrinth.registry;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class EventRegistry {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
        });
    }
}
