package dev.mayaqq.labyrinth.mixin;

import com.google.common.base.Predicates;
import dev.mayaqq.labyrinth.Labyrinth;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.player.PlayerEntity;
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
            } else {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                player.incrementStat(Stats.INTERACT_WITH_ANVIL);
            }
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
    private static BlockPattern getCompletedForgePattern() {
        if (COMPLETED_FORGE == null) {
            COMPLETED_FORGE = BlockPatternBuilder.start().aisle("?????", "?????", "?????", "?????", "?????")
                    .where('?', CachedBlockPosition.matchesBlockState(blockState -> blockState.isIn(Labyrinth.ANVIL_TRIGGER)))
                    .build();
        }

        return COMPLETED_FORGE;
    }
}
