package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.items.BlazeBowItem;
import dev.mayaqq.labyrinth.items.base.*;
import dev.mayaqq.labyrinth.registry.materials.CustomMaterials;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import static dev.mayaqq.labyrinth.Labyrinth.id;


public class ItemRegistry {

    private static final RegistryKey<ItemGroup> LABYRINTH_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, id("labyrinth"));

    // test sword
    public static final LabyrinthSwordItem TEST_SWORD =
            Registry.register(Registries.ITEM, id("test_sword"),
            new LabyrinthSwordItem(CustomMaterials.NETHERITE, 10001, 1000F, Items.NETHERITE_SWORD, new Item.Settings().maxDamage(-1), "test_sword"));
    // bows
    public static final BlazeBowItem BLAZE_BOW = Registry.register(Registries.ITEM, id("blaze_bow"),
            new BlazeBowItem(Items.BOW, "blaze_bow"));
    // warhammers
    private static final float warhammerAttackSpeed = -3.3F;
    public static final LabyrinthWarHammerItem IRON_WARHAMMER = Registry.register(Registries.ITEM, id("iron_warhammer"),
            new LabyrinthWarHammerItem(CustomMaterials.IRON, 12, warhammerAttackSpeed, Items.IRON_AXE, new Item.Settings().maxDamage(620) , "iron_warhammer"));
    public static final LabyrinthWarHammerItem GOLDEN_WARHAMMER = Registry.register(Registries.ITEM, id("golden_warhammer"),
            new LabyrinthWarHammerItem(CustomMaterials.GOLD, 13, warhammerAttackSpeed, Items.GOLDEN_AXE, new Item.Settings().maxDamage(100) , "golden_warhammer"));
    public static final LabyrinthWarHammerItem DIAMOND_WARHAMMER = Registry.register(Registries.ITEM, id("diamond_warhammer"),
            new LabyrinthWarHammerItem(CustomMaterials.DIAMOND, 15, warhammerAttackSpeed, Items.DIAMOND_AXE, new Item.Settings().maxDamage(3000) , "diamond_warhammer"));
    public static final LabyrinthWarHammerItem NETHERITE_WARHAMMER = Registry.register(Registries.ITEM, id("netherite_warhammer"),
            new LabyrinthWarHammerItem(CustomMaterials.NETHERITE, 17, warhammerAttackSpeed, Items.NETHERITE_AXE, new Item.Settings().maxDamage(3800) , "netherite_warhammer"));

    // hammers
    public static final float hammerAttackSpeed = -3.0F;
    public static final LabyrinthHammerItem IRON_HAMMER = Registry.register(Registries.ITEM, id("iron_hammer"),
            new LabyrinthHammerItem(CustomMaterials.IRON, 7, hammerAttackSpeed, Items.IRON_AXE, new Item.Settings().maxDamage(500) , "iron_hammer"));
    public static final LabyrinthHammerItem GOLDEN_HAMMER = Registry.register(Registries.ITEM, id("golden_hammer"),
            new LabyrinthHammerItem(CustomMaterials.GOLD, 8, hammerAttackSpeed, Items.GOLDEN_AXE, new Item.Settings().maxDamage(100) , "golden_hammer"));
    public static final LabyrinthHammerItem DIAMOND_HAMMER = Registry.register(Registries.ITEM, id("diamond_hammer"),
            new LabyrinthHammerItem(CustomMaterials.DIAMOND, 10, hammerAttackSpeed, Items.DIAMOND_AXE, new Item.Settings().maxDamage(2000) , "diamond_hammer"));
    public static final LabyrinthHammerItem NETHERITE_HAMMER = Registry.register(Registries.ITEM, id("netherite_hammer"),
            new LabyrinthHammerItem(CustomMaterials.NETHERITE, 12, hammerAttackSpeed, Items.NETHERITE_AXE, new Item.Settings().maxDamage(2500) , "netherite_hammer"));

    // war axes
    public static final float warAxeAttackSpeed = -3.0F;
    public static final LabyrinthWaraxeItem IRON_WARAXE = Registry.register(Registries.ITEM, id("iron_waraxe"),
            new LabyrinthWaraxeItem(CustomMaterials.IRON, 9, warAxeAttackSpeed, Items.IRON_AXE, new Item.Settings().maxDamage(600) , "iron_waraxe"));
    public static final LabyrinthWaraxeItem GOLDEN_WARAXE = Registry.register(Registries.ITEM, id("golden_waraxe"),
            new LabyrinthWaraxeItem(CustomMaterials.GOLD, 10, warAxeAttackSpeed, Items.GOLDEN_AXE, new Item.Settings().maxDamage(90) , "golden_waraxe"));
    public static final LabyrinthWaraxeItem DIAMOND_WARAXE = Registry.register(Registries.ITEM, id("diamond_waraxe"),
            new LabyrinthWaraxeItem(CustomMaterials.DIAMOND, 11, warAxeAttackSpeed, Items.DIAMOND_AXE, new Item.Settings().maxDamage(2000) , "diamond_waraxe"));
    public static final LabyrinthWaraxeItem NETHERITE_WARAXE = Registry.register(Registries.ITEM, id("netherite_waraxe"),
            new LabyrinthWaraxeItem(CustomMaterials.NETHERITE, 14, warAxeAttackSpeed, Items.NETHERITE_AXE, new Item.Settings().maxDamage(2800) , "netherite_waraxe"));

    // knife
    public static final float knifeAttackSpeed = -0.0F;
    public static final LabyrinthKnifeItem IRON_KNIFE = Registry.register(Registries.ITEM, id("iron_knife"),
            new LabyrinthKnifeItem(CustomMaterials.IRON, 1, knifeAttackSpeed, Items.IRON_SWORD, new Item.Settings().maxDamage(100) , "iron_knife"));
    public static final LabyrinthKnifeItem GOLDEN_KNIFE = Registry.register(Registries.ITEM, id("golden_knife"),
            new LabyrinthKnifeItem(CustomMaterials.GOLD, 2, knifeAttackSpeed, Items.GOLDEN_SWORD, new Item.Settings().maxDamage(18) , "golden_knife"));
    public static final LabyrinthKnifeItem DIAMOND_KNIFE = Registry.register(Registries.ITEM, id("diamond_knife"),
            new LabyrinthKnifeItem(CustomMaterials.DIAMOND, 3, knifeAttackSpeed, Items.DIAMOND_SWORD, new Item.Settings().maxDamage(220) , "diamond_knife"));
    public static final LabyrinthKnifeItem NETHERITE_KNIFE = Registry.register(Registries.ITEM, id("netherite_knife"),
            new LabyrinthKnifeItem(CustomMaterials.NETHERITE, 4, knifeAttackSpeed, Items.NETHERITE_SWORD, new Item.Settings().maxDamage(350) , "netherite_knife"));

    // spear
    public static final float spearAttackSpeed = -2.8F;
    public static final LabyrinthSpearItem IRON_SPEAR = Registry.register(Registries.ITEM, id("iron_spear"),
            new LabyrinthSpearItem(CustomMaterials.IRON, 6, spearAttackSpeed, Items.TRIDENT, new Item.Settings().maxDamage(250) , "iron_spear"));
    public static final LabyrinthSpearItem GOLDEN_SPEAR = Registry.register(Registries.ITEM, id("golden_spear"),
            new LabyrinthSpearItem(CustomMaterials.GOLD, 7, spearAttackSpeed, Items.TRIDENT, new Item.Settings().maxDamage(60) , "golden_spear"));
    public static final LabyrinthSpearItem DIAMOND_SPEAR = Registry.register(Registries.ITEM, id("diamond_spear"),
            new LabyrinthSpearItem(CustomMaterials.DIAMOND, 9, spearAttackSpeed, Items.TRIDENT, new Item.Settings().maxDamage(1500) , "diamond_spear"));
    public static final LabyrinthSpearItem NETHERITE_SPEAR = Registry.register(Registries.ITEM, id("netherite_spear"),
            new LabyrinthSpearItem(CustomMaterials.NETHERITE, 10, spearAttackSpeed, Items.TRIDENT, new Item.Settings().maxDamage(2000) , "netherite_spear"));

    // cleaver
    public static final float cleaverAttackSpeed = -2.7F;
    public static final LabyrinthCleaverItem IRON_CLEAVER = Registry.register(Registries.ITEM, id("iron_cleaver"),
            new LabyrinthCleaverItem(CustomMaterials.IRON, 6, cleaverAttackSpeed, Items.IRON_SWORD, new Item.Settings().maxDamage(600) , "iron_cleaver"));
    public static final LabyrinthCleaverItem GOLDEN_CLEAVER = Registry.register(Registries.ITEM, id("golden_cleaver"),
            new LabyrinthCleaverItem(CustomMaterials.GOLD, 7, cleaverAttackSpeed, Items.GOLDEN_SWORD, new Item.Settings().maxDamage(70) , "golden_cleaver"));
    public static final LabyrinthCleaverItem DIAMOND_CLEAVER = Registry.register(Registries.ITEM, id("diamond_cleaver"),
            new LabyrinthCleaverItem(CustomMaterials.DIAMOND, 9, cleaverAttackSpeed, Items.DIAMOND_SWORD, new Item.Settings().maxDamage(2000) , "diamond_cleaver"));
    public static final LabyrinthCleaverItem NETHERITE_CLEAVER = Registry.register(Registries.ITEM, id("netherite_cleaver"),
            new LabyrinthCleaverItem(CustomMaterials.NETHERITE, 10, cleaverAttackSpeed, Items.NETHERITE_SWORD, new Item.Settings().maxDamage(2800) , "netherite_cleaver"));

    // tomahawk
    public static final float tomahawkAttackSpeed = -2.0F;
    public static final LabyrinthTomahawkItem IRON_TOMAHAWK = Registry.register(Registries.ITEM, id("iron_tomahawk"),
            new LabyrinthTomahawkItem(CustomMaterials.IRON, 6, tomahawkAttackSpeed, Items.IRON_AXE, new Item.Settings().maxDamage(250) , "iron_tomahawk"));
    public static final LabyrinthTomahawkItem GOLDEN_TOMAHAWK = Registry.register(Registries.ITEM, id("golden_tomahawk"),
            new LabyrinthTomahawkItem(CustomMaterials.GOLD, 7, tomahawkAttackSpeed, Items.GOLDEN_AXE, new Item.Settings().maxDamage(40) , "golden_tomahawk"));
    public static final LabyrinthTomahawkItem DIAMOND_TOMAHAWK = Registry.register(Registries.ITEM, id("diamond_tomahawk"),
            new LabyrinthTomahawkItem(CustomMaterials.DIAMOND, 8, tomahawkAttackSpeed, Items.DIAMOND_AXE, new Item.Settings().maxDamage(1500) , "diamond_tomahawk"));
    public static final LabyrinthTomahawkItem NETHERITE_TOMAHAWK = Registry.register(Registries.ITEM, id("netherite_tomahawk"),
            new LabyrinthTomahawkItem(CustomMaterials.NETHERITE, 9, tomahawkAttackSpeed, Items.NETHERITE_AXE, new Item.Settings().maxDamage(2000) , "netherite_tomahawk"));

    // katana
    public static final float katanaAttackSpeed = -2.2F;
    public static final LabyrinthKatanaItem IRON_KATANA = Registry.register(Registries.ITEM, id("iron_katana"),
            new LabyrinthKatanaItem(CustomMaterials.IRON, 7, katanaAttackSpeed, Items.IRON_SWORD, new Item.Settings().maxDamage(600) , "iron_katana"));
    public static final LabyrinthKatanaItem GOLDEN_KATANA = Registry.register(Registries.ITEM, id("golden_katana"),
            new LabyrinthKatanaItem(CustomMaterials.GOLD, 8, katanaAttackSpeed, Items.GOLDEN_SWORD, new Item.Settings().maxDamage(70) , "golden_katana"));
    public static final LabyrinthKatanaItem DIAMOND_KATANA = Registry.register(Registries.ITEM, id("diamond_katana"),
            new LabyrinthKatanaItem(CustomMaterials.DIAMOND, 10, katanaAttackSpeed, Items.DIAMOND_SWORD, new Item.Settings().maxDamage(2000) , "diamond_katana"));
    public static final LabyrinthKatanaItem NETHERITE_KATANA = Registry.register(Registries.ITEM, id("netherite_katana"),
            new LabyrinthKatanaItem(CustomMaterials.NETHERITE, 11, katanaAttackSpeed, Items.NETHERITE_SWORD, new Item.Settings().maxDamage(2800) , "netherite_katana"));

    public static void register() {
        // puts all items from this mod into a creative tab
        Registry.register(Registries.ITEM_GROUP, LABYRINTH_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("creative.labyrinth.group"))
                .icon(() -> new ItemStack(IRON_WARHAMMER))
                .entries((group, entries) -> {
                    for (Item item : Registries.ITEM) {
                        if (item instanceof LabyrinthItem) {
                            entries.add(item);
                        }
                    }
                })
                .build()
        );
        PolymerItemGroupUtils.registerPolymerItemGroup(id("labyrinth"), Registries.ITEM_GROUP.get(LABYRINTH_GROUP));
        // item mod asset registration
        PolymerResourcePackUtils.addModAssets("labyrinth");
    }
}
