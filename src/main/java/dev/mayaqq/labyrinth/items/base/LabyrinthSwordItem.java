package dev.mayaqq.labyrinth.items.base;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class LabyrinthSwordItem extends SwordItem implements LabyrinthItem {
    private final Item polymerItem;
    private final int modelData;
    public LabyrinthSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.polymerItem = polymerItem;
        this.modelData = PolymerResourcePackUtils.requestModel(polymerItem, id("item/" + id)).value();
    }
    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.polymerItem;
    }
    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.modelData;
    }
}
