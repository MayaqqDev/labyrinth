package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.entities.SpearEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
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
            return new SpearEntity(type, world, null);
        }
    }).dimensions(EntityType.TRIDENT.getDimensions()).build());
    public static void register() {
        PolymerEntityUtils.registerType(SPEAR);

    }
}
