package dev.mayaqq.labyrinth.items;

import dev.mayaqq.labyrinth.items.base.LabyrinthItem;
import dev.mayaqq.labyrinth.registry.materials.CustomArmorMaterials;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class EnderCrownItem extends ArmorItem implements LabyrinthItem {
    private final Item polymerItem;

    public EnderCrownItem(Item polymerItem, Settings settings, String id) {
        super(CustomArmorMaterials.ENDER, Type.HELMET, settings);
        this.polymerItem = polymerItem;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.polymerItem;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.labyrinth.ender_crown.tooltip").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        tooltip.add(Text.of(" "));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        stack.addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier("GenericMaxHealth", 8.0F, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.HEAD);
        stack.addAttributeModifier(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier("GenericArmor", 4.0F, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.HEAD);
        stack.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier("GenericArmorToughness", 2.0F, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.HEAD);
        stack.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier("GenericKnockbackResistance", 0.1F, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.HEAD);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
