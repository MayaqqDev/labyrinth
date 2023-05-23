package dev.mayaqq.labyrinth.items.base;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class LabyrinthSwordItem extends SwordItem implements PolymerItem, LabyrinthItem {
    private final Item polymerItem;
    private final int customModelData;
    public LabyrinthSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, String id) {
        super(toolMaterial, attackDamage, attackSpeed, new Settings());
        this.polymerItem = polymerItem;
        this.customModelData = PolymerResourcePackUtils.requestModel(polymerItem, id("item/" + id)).value();
    }
    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.polymerItem;
    }
    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.customModelData;
    }
}
