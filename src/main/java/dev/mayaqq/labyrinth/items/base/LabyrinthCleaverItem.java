package dev.mayaqq.labyrinth.items.base;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class LabyrinthCleaverItem extends LabyrinthSwordItem {
    public LabyrinthCleaverItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, polymerItem, settings, id);
    }
    @Override
    public void onCraft(ItemStack itemStack, World world, net.minecraft.entity.player.PlayerEntity playerEntity) {
        itemStack.addEnchantment(Enchantments.SMITE, 4);
    }
}
