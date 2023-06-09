package dev.mayaqq.labyrinth.items.base;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class LabyrinthKatanaItem extends LabyrinthSwordItem {
    private boolean wasOnGround = true;
    public ServerPlayerEntity player;
    public LabyrinthKatanaItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, polymerItem, settings, id);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.labyrinth.katana.tooltip").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        tooltip.add(Text.of(" "));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        player = (ServerPlayerEntity) user;
        user.getItemCooldownManager().set(this, 60);
        if (!world.isClient && wasOnGround) {
            wasOnGround = false;
            ServerPlayerEntity player = (ServerPlayerEntity) user;
            Vec3d vec = player.getRotationVector();
            double multiplier = 1.5;
            player.setVelocity(vec.x * multiplier, vec.y * multiplier, vec.z * multiplier);
            player.velocityDirty = true;
            player.velocityModified = true;
            itemStack.damage(1, user, (e) -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, false);
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected && !wasOnGround && entity.isOnGround()) {
            wasOnGround = true;
        }
    }
}
