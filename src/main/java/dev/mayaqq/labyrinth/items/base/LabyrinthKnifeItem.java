package dev.mayaqq.labyrinth.items.base;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

public class LabyrinthKnifeItem extends LabyrinthSwordItem {
    public LabyrinthKnifeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, polymerItem, settings, id);
    }
}