package dev.mayaqq.labyrinth;

import dev.mayaqq.labyrinth.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Labyrinth implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Labyrinth");

    public static Identifier id(String path) {
        return new Identifier("labyrinth", path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Labyrinth is initializing...");
        ItemRegistry.register();
        TagRegistry.register();
        RecipeRegistry.register();
        EventRegistry.register();
        EntityRegistry.register();
    }
}
