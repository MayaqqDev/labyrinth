package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.Labyrinth;
import dev.mayaqq.labyrinth.entities.SpearEntity;
import dev.mayaqq.labyrinth.entities.TomahawkEntity;
import dev.mayaqq.labyrinth.registry.materials.CustomMaterials;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class EntityRegistry {
    public static final EntityType<SpearEntity> SPEAR = Registry.register(Registries.ENTITY_TYPE, id("spear"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, new EntityType.EntityFactory<SpearEntity>() {
        @Override
        public SpearEntity create(EntityType<SpearEntity> type, World world) {
            return new SpearEntity(type, world, null, 0);
        }
    }).dimensions(EntityType.TRIDENT.getDimensions()).build());

    public static final EntityType<TomahawkEntity> TOMAHAWK = Registry.register(Registries.ENTITY_TYPE, id("tomahawk"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, new EntityType.EntityFactory<TomahawkEntity>() {
        @Override
        public TomahawkEntity create(EntityType<TomahawkEntity> type, World world) {
            return new TomahawkEntity(type, world, ItemRegistry.IRON_TOMAHAWK.getDefaultStack(), CustomMaterials.IRON, 0);
        }
    }).dimensions(EntityType.TRIDENT.getDimensions()).build());

    public static void register() {
        PolymerEntityUtils.registerType(SPEAR);

    }
}
