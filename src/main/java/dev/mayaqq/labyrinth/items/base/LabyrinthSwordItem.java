package dev.mayaqq.labyrinth.items.base;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class LabyrinthSwordItem extends SwordItem implements PolymerItem, LabyrinthItem {
    private final Item polymerItem;
    private final String id;
    public LabyrinthSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.polymerItem = polymerItem;
        this.id = id;
    }
    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.polymerItem;
    }
    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.requestModel(polymerItem, id("item/" + id)).value();
    }
}
