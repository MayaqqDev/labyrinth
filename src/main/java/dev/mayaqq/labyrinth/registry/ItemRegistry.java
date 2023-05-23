package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.items.BlazeBowItem;
import dev.mayaqq.labyrinth.items.base.LabyrinthItem;
import dev.mayaqq.labyrinth.items.base.LabyrinthSwordItem;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import static dev.mayaqq.labyrinth.Labyrinth.id;


public class ItemRegistry {

    // items
    public static final LabyrinthSwordItem TEST_SWORD =
            Registry.register(Registries.ITEM, id("test_sword"),
            new LabyrinthSwordItem(ToolMaterials.IRON, 2020, 0F, Items.IRON_SWORD, "test_sword"));
    public static final BlazeBowItem BLAZE_BOW = Registry.register(Registries.ITEM, id("blaze_bow"),
            new BlazeBowItem(Items.BOW, "blaze_bow"));

    public static void register() {
        // for the creative tab
        PolymerItemGroupUtils.builder(id("labyrinth"))
                .displayName(Text.translatable("creative.labyrinth.group"))
                .icon(() -> new ItemStack(Items.IRON_SWORD))
                .entries((enabledFeatures, entries) -> {
                    for (Item item : Registries.ITEM) {
                        if (item instanceof LabyrinthItem) {
                            entries.add(item);
                        }
                    }
                })
                .build();

        // item mod asset registration
        PolymerResourcePackUtils.addModAssets("labyrinth");
    }
}
