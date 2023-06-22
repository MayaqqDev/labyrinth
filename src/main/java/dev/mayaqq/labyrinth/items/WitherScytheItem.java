package dev.mayaqq.labyrinth.items;

import dev.mayaqq.labyrinth.entities.WitherScytheSkullEntity;
import dev.mayaqq.labyrinth.items.base.LabyrinthSwordItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
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

public class WitherScytheItem extends LabyrinthSwordItem {
    public WitherScytheItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, polymerItem, settings, id);
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.labyrinth.wither_scythe.tooltip").formatted(Formatting.DARK_PURPLE).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("item.labyrinth.wither_scythe.tooltip2").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        tooltip.add(Text.of(" "));
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHit(stack, target, attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1), stack.getHolder());
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ServerPlayerEntity player = (ServerPlayerEntity) user;
        user.getItemCooldownManager().set(this, 120);
        Vec3d vec = player.getRotationVector();
        WitherScytheSkullEntity skull = new WitherScytheSkullEntity(world, user, vec.x, vec.y, vec.z);
        // set the skull's position to the player's position but add 1 to the direction the player is looking
        skull.updatePosition(user.getX() + vec.x, user.getY() + vec.y + 1, user.getZ() + vec.z);
        skull.setOwner(user);
        world.spawnEntity(skull);
        if (!world.isClient) {
            itemStack.damage(1, user, (e) -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, true);
    }

}
