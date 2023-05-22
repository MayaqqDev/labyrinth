package dev.mayaqq.labyrinth.mixin;

import dev.mayaqq.labyrinth.Labyrinth;
import dev.mayaqq.labyrinth.gui.ForgeGui;
import dev.mayaqq.labyrinth.registry.TagRegistry;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    private static BlockPattern COMPLETED_FORGE;
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void labyrinth$onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            // get the block below the anvil
            BlockPos blockPos = pos.down();
            if (this.getCompletedForgePattern().searchAround(world, blockPos) != null) {
                ForgeGui.gui((ServerPlayerEntity) player, pos);
            } else {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                player.incrementStat(Stats.INTERACT_WITH_ANVIL);
            }
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
    private static BlockPattern getCompletedForgePattern() {
        if (COMPLETED_FORGE == null) {
            COMPLETED_FORGE = BlockPatternBuilder.start()
                    .aisle("bbbbb", "bbsbb", "bsosb", "bbsbb", "bbbbb")
                    .aisle("b???b", "?????", "??a??", "?????", "b???b")
                    .aisle("w???w", "?????", "?????", "?????", "w???w")
                    .aisle("w???w", "?????", "??w??", "?????", "w???w")
                    .aisle("bs?sb", "s???s", "??c??", "s???s", "bs?sb")
                    .aisle("?bbb?", "bbbbb", "bbbbb", "bbbbb", "?bbb?")
                    .where('o', CachedBlockPosition.matchesBlockState(blockState -> blockState.isIn(TagRegistry.FORGE_BASE)))
                    .where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY))
                    .where('b', CachedBlockPosition.matchesBlockState(blockState -> blockState.isIn(TagRegistry.FORGE_BRICKS)))
                    .where('s', CachedBlockPosition.matchesBlockState(blockState -> blockState.isIn(TagRegistry.FORGE_STAIRS)))
                    .where('a', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.ANVIL)))
                    .where('w', CachedBlockPosition.matchesBlockState(blockState -> blockState.isIn(TagRegistry.FORGE_WALLS)))
                    .where('c', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.CHAIN)))
                    .build();
        }

        return COMPLETED_FORGE;
    }
}
