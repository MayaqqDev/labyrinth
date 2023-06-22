package dev.mayaqq.labyrinth.items;

import dev.mayaqq.labyrinth.Labyrinth;
import dev.mayaqq.labyrinth.items.base.LabyrinthItem;
import dev.mayaqq.labyrinth.utils.Multithreading;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class MagicMirrorItem extends Item implements LabyrinthItem {
    private final Item polymerItem;
    private final int modelData;
    public MagicMirrorItem(Item polymerItem, Item.Settings settings, String id) {
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
        tooltip.add(Text.translatable("item.labyrinth.magic_mirror.tooltip").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        tooltip.add(Text.of(" "));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));
        ItemStack itemStack = user.getStackInHand(hand);
        ServerPlayerEntity player = (ServerPlayerEntity) user;
        user.getItemCooldownManager().set(this, 600);
        BlockPos pos = player.getSpawnPointPosition();
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 1.0F, 1.0F);
        Multithreading.schedule(() -> {
            player.teleport(player.getServer().getWorld(player.getSpawnPointDimension()), pos.getX(), pos.getY(), pos.getZ(), player.getYaw(), player.getPitch());
            world.addParticle(ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 0.0D, 0.4D, 0.0D);
        }, 5, TimeUnit.SECONDS);
        return TypedActionResult.success(itemStack);
    }
}
