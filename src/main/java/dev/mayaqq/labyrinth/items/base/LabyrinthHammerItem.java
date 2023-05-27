package dev.mayaqq.labyrinth.items.base;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

public class LabyrinthHammerItem extends LabyrinthSwordItem {
    public LabyrinthHammerItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item item, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, item, settings, id);
    }
}