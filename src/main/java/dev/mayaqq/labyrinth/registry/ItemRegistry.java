package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.items.BlazeBowItem;
import dev.mayaqq.labyrinth.items.base.LabyrinthHammerItem;
import dev.mayaqq.labyrinth.items.base.LabyrinthItem;
import dev.mayaqq.labyrinth.items.base.LabyrinthSwordItem;
import dev.mayaqq.labyrinth.registry.materials.CustomMaterials;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import static dev.mayaqq.labyrinth.Labyrinth.id;


public class ItemRegistry {

    // item registration
    public static final LabyrinthSwordItem TEST_SWORD =
            Registry.register(Registries.ITEM, id("test_sword"),
            new LabyrinthSwordItem(ToolMaterials.IRON, 2020, 0F, Items.IRON_SWORD, new Item.Settings(), "test_sword"));
    public static final BlazeBowItem BLAZE_BOW = Registry.register(Registries.ITEM, id("blaze_bow"),
            new BlazeBowItem(Items.BOW, "blaze_bow"));
    public static final LabyrinthHammerItem IRON_WARHAMMER = Registry.register(Registries.ITEM, id("iron_warhammer"),
            new LabyrinthHammerItem(CustomMaterials.IRON, 15, -3.6F, Items.IRON_AXE, new Item.Settings().maxDamage(620) , "iron_warhammer"));
    public static final LabyrinthHammerItem GOLDEN_WARHAMMER = Registry.register(Registries.ITEM, id("golden_warhammer"),
            new LabyrinthHammerItem(CustomMaterials.GOLD, 15, -3.6F, Items.GOLDEN_AXE, new Item.Settings().maxDamage(100) , "golden_warhammer"));
    public static final LabyrinthHammerItem DIAMOND_WARHAMMER = Registry.register(Registries.ITEM, id("diamond_warhammer"),
            new LabyrinthHammerItem(CustomMaterials.DIAMOND, 17, -3.6F, Items.DIAMOND_AXE, new Item.Settings().maxDamage(3000) , "diamond_warhammer"));
    public static final LabyrinthHammerItem NETHERITE_WARHAMMER = Registry.register(Registries.ITEM, id("netherite_warhammer"),
            new LabyrinthHammerItem(CustomMaterials.NETHERITE, 22, -3.6F, Items.NETHERITE_AXE, new Item.Settings().maxDamage(3800) , "netherite_warhammer"));

    public static void register() {
        // puts all items from this mod into a creative tab
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
