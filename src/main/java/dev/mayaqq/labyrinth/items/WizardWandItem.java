package dev.mayaqq.labyrinth.items;

import dev.mayaqq.labyrinth.items.base.LabyrinthItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class WizardWandItem extends Item implements LabyrinthItem {
    private final Item polymerItem;
    private final int modelData;
    public WizardWandItem(Item polymerItem, Item.Settings settings, String id) {
        super(settings);
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

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.labyrinth.wizard_wand.tooltip").formatted(Formatting.DARK_PURPLE).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("item.labyrinth.wizard_wand.tooltip2").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        tooltip.add(Text.of(" "));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ServerPlayerEntity player = (ServerPlayerEntity) user;
        user.getItemCooldownManager().set(this, 120);
        Vec3d vec = player.getRotationVector();
        FireballEntity ball = new FireballEntity(world, user, vec.x, vec.y, vec.z, 1);
        ball.updatePosition(user.getX() + vec.x, user.getY() + vec.y + 1, user.getZ() + vec.z);
        ball.setOwner(user);
        world.spawnEntity(ball);
        if (!world.isClient) {
            itemStack.damage(1, user, (e) -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, true);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            if (entity.isPlayer()) {
                boolean holding = ((PlayerEntity) entity).getOffHandStack().getItem() == this || selected;
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                player.setInvisible(holding && player.isSneaking());
            }
        }
    }
}
