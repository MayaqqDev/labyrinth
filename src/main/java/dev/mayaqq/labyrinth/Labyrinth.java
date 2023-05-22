package dev.mayaqq.labyrinth;

import dev.mayaqq.labyrinth.registry.ItemRegistry;
import dev.mayaqq.labyrinth.registry.MaterialRegistry;
import dev.mayaqq.labyrinth.registry.RecipeRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Labyrinth implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Labyrinth");
    public static final TagKey<Block> ANVIL_TRIGGER = TagKey.of(RegistryKeys.BLOCK, id("anvil_trigger"));


    public static Identifier id(String path) {
        return new Identifier("labyrinth", path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Labyrinth is initializing...");
        ItemRegistry.register();
        MaterialRegistry.register();
        RecipeRegistry.register();
    }
}
