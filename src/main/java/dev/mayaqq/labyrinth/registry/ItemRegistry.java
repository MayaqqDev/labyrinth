package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.Labyrinth;
import dev.mayaqq.labyrinth.items.base.LabyrinthSwordItem;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.AnvilBlock;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;


public class ItemRegistry {

    public static final LabyrinthSwordItem TEST_SWORD = Registry.register(Registries.ITEM, Labyrinth.id("test_sword"), new LabyrinthSwordItem(ToolMaterials.IRON, 2020, 0F, new Item.Settings().maxCount(1), Items.IRON_SWORD, 2));

    public static void register() {
        PolymerItemGroupUtils.builder(Labyrinth.id("labyrinth"))
                .displayName(Text.translatable("creative.labyrinth.group"))
                .icon(() -> new ItemStack(Items.IRON_SWORD))
                .entries((enabledFeatures, entries) -> {
                    entries.add(TEST_SWORD);
                })
                .build();

        // item textures
        PolymerResourcePackUtils.addModAssets("labyrinth");
        PolymerResourcePackUtils.requestModel(Items.IRON_SWORD, Labyrinth.id("item/test_sword"));
    }
}
