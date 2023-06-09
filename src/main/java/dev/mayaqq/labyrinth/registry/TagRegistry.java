package dev.mayaqq.labyrinth.registry;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class TagRegistry {
    public static final TagKey<Block> FORGE_BASE = TagKey.of(RegistryKeys.BLOCK, id("forge_base"));
    public static final TagKey<Block> FORGE_BRICKS = TagKey.of(RegistryKeys.BLOCK, id("forge_bricks"));
    public static final TagKey<Block> FORGE_STAIRS = TagKey.of(RegistryKeys.BLOCK, id("forge_stairs"));
    public static final TagKey<Block> FORGE_WALLS = TagKey.of(RegistryKeys.BLOCK, id("forge_walls"));
    public static final TagKey<Block> FORGE_SLABS = TagKey.of(RegistryKeys.BLOCK, id("forge_slabs"));


    public static void register() {}
}
