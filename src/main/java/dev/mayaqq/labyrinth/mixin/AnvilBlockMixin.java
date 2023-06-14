package dev.mayaqq.labyrinth.mixin;

import dev.mayaqq.labyrinth.gui.ForgeGui;
import dev.mayaqq.labyrinth.registry.TagRegistry;
import dev.mayaqq.nexusframe.api.multiblock.Multiblock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.function.Predicate;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    private static char[][][] COMPLETED_FORGE;
    private static final HashMap<BlockPos, Multiblock> forgeMultiblocks = new HashMap<>();
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void labyrinth$onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            getForgeMultiblock(pos);
            // get the block below the anvil
            BlockPos blockPos = pos.down();
            if (world.getBlockState(blockPos).isIn(TagRegistry.FORGE_BASE)) {
                Multiblock forge = forgeMultiblocks.get(pos);
                if (player.isInPose(EntityPose.CROUCHING)) {
                    forge.rotate();
                    if (forge.getPreviewed()) {
                        forge.check(pos, world);
                    }
                } else if (forge.check(pos, world)) {
                    ForgeGui.gui((ServerPlayerEntity) player, pos);
                } else {
                    player.sendMessage(Text.translatable("gui.labyrinth.forge.message.negative.forge"), true);
                }
            } else {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
            player.incrementStat(Stats.INTERACT_WITH_ANVIL);
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }

    private static char[][][] getForge() {
        if (COMPLETED_FORGE == null) {
            COMPLETED_FORGE = new char[][][]{
                    {
                            {'a', 'a', 'a', 'a', 's', 's', 's', 's', 's', 's', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 's', 's', 'b', 'b', 'b', 'b', 'b', 's', 's', 'a', 'a', 'a'},
                            {'a', 'a', 's', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 's', 'a', 'a'},
                            {'s', 's', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 's', 's'},
                            {'s', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's'},
                            {'s', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's'},
                            {'s', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's'},
                            {'s', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's'},
                            {'s', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's'},
                            {'s', 's', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 's', 's'},
                            {'a', 'a', 'a', 's', 's', 'b', 'b', 'b', 'b', 'b', 's', 's', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 's', 's', 's', 's', 's', 's', 'a', 'a', 'a', 'a'},
                    },
                    {
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 's', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'b', 'a', 'b', 'b', 'b', 'b', 'b', 'a', 'b', 'a', 'a', 'a'},
                            {'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a'},
                            {'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a'},
                            {'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a'},
                            {'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a'},
                            {'a', 'a', 's', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 's', 'a', 'a'},
                            {'a', 'a', 'a', 'b', 'a', 'b', 'b', 'b', 'b', 'b', 'a', 'b', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 's', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                    },
                    {
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'f', 'a', 's', 's', 's', 's', 's', 'a', 'f', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'c', 'c', 'c', 'c', 'c', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'c', 'c', 's', 'c', 'c', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'c', 's', 'o', 's', 'c', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'c', 'c', 's', 'c', 'c', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 's', 'c', 'c', 'c', 'c', 'c', 's', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'f', 'a', 's', 's', 's', 's', 's', 'a', 'f', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                    },
                    {
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', '$', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                    },
                    {
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a', 'a', 'f', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                    },
                    {
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'c', 'l', 'c', 'a', 'a', 'a', 'c', 'l', 'c', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'c', 'm', 'a', 'a', 'a', 'm', 'c', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'c', 'm', 'a', 'a', 'a', 'm', 'c', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'c', 'l', 'c', 'a', 'a', 'a', 'c', 'l', 'c', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                    },
                    {
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'm', 'a', 'a', 'a', 'a', 'a', 'm', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'c', 'a', 'c', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'c', 'a', 'c', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'c', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'm', 'a', 'a', 'a', 'a', 'a', 'm', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                    },
                    {
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'm', 'b', 'm', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'b', 'g', 'b', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'm', 'b', 'm', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                            {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'},
                    }
            };
        }
        return COMPLETED_FORGE;
    }
    private  static HashMap<Character, Predicate<BlockState>> getPredicates() {
        HashMap<Character, Predicate<BlockState>> predicates = new HashMap<>();
        predicates.put('a', BlockStatePredicate.ANY);
        predicates.put('o', blockState -> blockState.isIn(TagRegistry.FORGE_BASE));
        predicates.put('$', BlockStatePredicate.forBlock(Blocks.ANVIL));
        predicates.put('b', BlockStatePredicate.forBlock(Blocks.BLACKSTONE));
        predicates.put('c', BlockStatePredicate.forBlock(Blocks.POLISHED_BLACKSTONE_BRICKS));
        predicates.put('m', BlockStatePredicate.forBlock(Blocks.POLISHED_BLACKSTONE_SLAB));
        predicates.put('f', BlockStatePredicate.forBlock(Blocks.POLISHED_BLACKSTONE_WALL));
        predicates.put('s', BlockStatePredicate.forBlock(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS));
        predicates.put('l', BlockStatePredicate.forBlock(Blocks.LAVA));
        predicates.put('g', BlockStatePredicate.forBlock(Blocks.GILDED_BLACKSTONE));
        return predicates;
    }
    private static void getForgeMultiblock(BlockPos pos) {
        if (forgeMultiblocks.get(pos) == null) {
            forgeMultiblocks.put(pos, new Multiblock(getForge(), getPredicates(), true, false));
        }
    }
}