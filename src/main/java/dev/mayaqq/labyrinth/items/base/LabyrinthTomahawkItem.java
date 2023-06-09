package dev.mayaqq.labyrinth.items.base;

import dev.mayaqq.labyrinth.entities.SpearEntity;
import dev.mayaqq.labyrinth.entities.TomahawkEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class LabyrinthTomahawkItem extends LabyrinthWaraxeItem {
    public LabyrinthTomahawkItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item polymerItem, Settings settings, String id) {
        super(toolMaterial, attackDamage, attackSpeed, polymerItem, settings, id);
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.labyrinth.tomahawk.tooltip").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        tooltip.add(Text.of(" "));
    }
    // This is to be done later
    /*
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.getItemCooldownManager().set(this, 60);
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            itemStack.damage(1, user, (p) -> {
                p.sendToolBreakStatus(user.getActiveHand());
            });
            int slot = 0;
            for (int y = 0; y < user.getInventory().size(); y++) {
                if (user.getInventory().getStack(y) == itemStack) {
                    slot = y;
                }
            }
            TomahawkEntity tomahawkEntity = TomahawkEntity.create(world, user, itemStack, getMaterial(), slot, user.getBlockPos());
            tomahawkEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            if (user.getAbilities().creativeMode) {
                tomahawkEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }

            world.spawnEntity(tomahawkEntity);
            world.playSoundFromEntity(null, tomahawkEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
            if (!user.getAbilities().creativeMode) {
                user.getInventory().removeOne(itemStack);
            }
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.consume(itemStack);
    }

     */
}
